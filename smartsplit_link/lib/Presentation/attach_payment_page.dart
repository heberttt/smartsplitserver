import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:http/http.dart' as http;
import 'package:smartsplit_link/Model/Friend.dart';
import 'package:smartsplit_link/Model/guest_friend.dart';
import 'package:smartsplit_link/Model/registered_friend.dart';
import 'package:smartsplit_link/Model/split_bill.dart';
import 'package:path/path.dart' as path;
import 'package:smartsplit_link/Service/split_service.dart';


class AttachPaymentPage extends StatefulWidget {
  const AttachPaymentPage(this.splitBill, this.friend, {super.key});
  final SplitBill splitBill;
  final Friend friend;

  @override
  State<AttachPaymentPage> createState() => _AttachPaymentPageState();
}

class _AttachPaymentPageState extends State<AttachPaymentPage> {
  String? _imageFile;
  bool _isUploading = false;

  bool _hasPaid = false;
  String? _paymentImageUrl;

  final SplitService _splitService = SplitService();

  @override
  void initState() {
    super.initState();

    final friendPayment = widget.splitBill.members.firstWhere((m) {
      if (m.friend is RegisteredFriend && widget.friend is RegisteredFriend) {
        return (m.friend as RegisteredFriend).id ==
            (widget.friend as RegisteredFriend).id;
      } else if (m.friend is GuestFriend && widget.friend is GuestFriend) {
        return (m.friend as GuestFriend).name ==
            (widget.friend as GuestFriend).name;
      }
      return false;
    }, orElse: () => throw Exception("Current user not found in bill members"));

    _hasPaid = friendPayment.hasPaid;
    _paymentImageUrl = friendPayment.paymentImageLink;
  }

  Future<void> _pickImage() async {
    final pickedFile = await ImagePicker().pickImage(
      source: ImageSource.gallery,
    );
    if (pickedFile != null) {
      setState(() {
        _imageFile = pickedFile.path;
      });
    }
  }

  Future<void> _approvePayment() async {
    if (_imageFile == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please select an image first.')),
      );
      return;
    }

    final confirm = await showDialog<bool>(
      context: context,
      builder: (_) => AlertDialog(
        title: const Text("Confirm Payment"),
        content: const Text(
          "Are you sure you want to submit this payment proof?",
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context, false),
            child: const Text("Cancel"),
          ),
          ElevatedButton(
            onPressed: () => Navigator.pop(context, true),
            child: const Text("Confirm"),
          ),
        ],
      ),
    );

    if (confirm != true) return;

    setState(() {
      _isUploading = true;
    });

    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (_) => const Center(child: CircularProgressIndicator()),
    );

    String token = widget.splitBill.publicAccessToken;
    String guestFriendName = (widget.friend as GuestFriend).name;

    final success = await _splitService.uploadPayment(
      widget.splitBill.id,
      token,
      _imageFile!,
      guestFriendName,
    );

    Navigator.pop(context);

    if (success) {
      try {
        final SplitBill? updatedBill = await _splitService.getMySplitBill(
          widget.splitBill.id,
          token,
        );

        if (updatedBill != null) {
          setState(() {
            _hasPaid = true;
            final updatedFriendPayment = updatedBill.members.firstWhere((m) {
              if (m.friend is RegisteredFriend && widget.friend is RegisteredFriend) {
                return (m.friend as RegisteredFriend).id == (widget.friend as RegisteredFriend).id;
              } else if (m.friend is GuestFriend && widget.friend is GuestFriend) {
                return (m.friend as GuestFriend).name == (widget.friend as GuestFriend).name;
              }
              return false;
            });
            _paymentImageUrl = updatedFriendPayment.paymentImageLink;
          });
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('Payment submitted successfully!')),
          );
          Navigator.pop(context, updatedBill);
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('Payment submitted, but failed to refresh bill data.')),
          );
        }
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Payment submitted, but error refreshing bill: $e')),
        );
      }
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Failed to submit payment.')),
      );
    }

    setState(() {
      _isUploading = false;
    });
  }

  Widget _buildImageDisplay() {
    if (_hasPaid && _paymentImageUrl != null) {
      return Image.network(_paymentImageUrl!, fit: BoxFit.contain);
    } else if (_imageFile != null) {
      return Image.network(_imageFile!, fit: BoxFit.contain);
    } else {
      return Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(Icons.image, size: 60, color: Colors.grey[400]),
          const SizedBox(height: 8.0),
          Text(
            'No payment proof attached',
            style: TextStyle(color: Colors.grey[600], fontSize: 16),
          ),
        ],
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final myDebt = widget.splitBill.members
        .where((m) {
          if (m.friend is RegisteredFriend &&
              widget.friend is RegisteredFriend) {
            return (m.friend as RegisteredFriend).id ==
                (widget.friend as RegisteredFriend).id;
          } else if (m.friend is GuestFriend && widget.friend is GuestFriend) {
            return (m.friend as GuestFriend).name ==
                (widget.friend as GuestFriend).name;
          }
          return false;
        })
        .fold<int>(0, (sum, m) => sum + m.totalDebt);

    return Scaffold(
      backgroundColor: Theme.of(context).colorScheme.surface,
      appBar: AppBar(
        centerTitle: true,
        title: const Text(
          "Attach Payment",
          style: TextStyle(color: Colors.white),
        ),
        toolbarHeight: 70,
        backgroundColor: Theme.of(context).primaryColor,
        leading: GestureDetector(
          onTap: () => Navigator.pop(context),
          behavior: HitTestBehavior.opaque,
          child: const Padding(
            padding: EdgeInsets.all(10),
            child: Icon(Icons.arrow_back, size: 35, color: Colors.white),
          ),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Expanded(
              child: Container(
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(8.0),
                ),
                child: Center(child: _buildImageDisplay()),
              ),
            ),
            const SizedBox(height: 20),
            const SizedBox(height: 20),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Text(
                  'Total amount',
                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.w500),
                ),
                Text(
                  'RM${(myDebt / 100).toStringAsFixed(2)}',
                  style: const TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 30),
            !_hasPaid
                ? ElevatedButton(
                    onPressed: (_hasPaid || _isUploading) ? null : _pickImage,
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.grey[100],
                      foregroundColor: Colors.black,
                      padding: const EdgeInsets.symmetric(vertical: 15),
                    ),
                    child: const Text(
                      "Choose Image",
                      style: TextStyle(fontWeight: FontWeight.w500),
                    ),
                  )
                : const SizedBox(height: 0),
            const SizedBox(height: 10),
            ElevatedButton(
              onPressed: (_hasPaid || _isUploading || _imageFile == null) ? null : _approvePayment,
              style: ElevatedButton.styleFrom(
                backgroundColor: Theme.of(context).primaryColor,
                foregroundColor: Colors.white,
                padding: const EdgeInsets.symmetric(vertical: 15),
              ),
              child:
                  _isUploading
                      ? const CircularProgressIndicator(color: Colors.white)
                      : Text(
                          _hasPaid
                              ? "Payment already submitted"
                              : "I have paid the bill",
                          style: const TextStyle(fontWeight: FontWeight.bold),
                        ),
            ),
          ],
        ),
      ),
    );
  }
}
