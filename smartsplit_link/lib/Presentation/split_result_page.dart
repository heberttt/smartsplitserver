import 'package:flutter/material.dart';
import 'package:smartsplit_link/Model/Friend.dart';
import 'package:smartsplit_link/Model/friend_payment.dart';
import 'package:smartsplit_link/Model/friend_split.dart';
import 'package:smartsplit_link/Model/guest_friend.dart';
import 'package:smartsplit_link/Model/receipt.dart';
import 'package:smartsplit_link/Model/receipt_item.dart';
import 'package:smartsplit_link/Model/registered_friend.dart';
import 'package:smartsplit_link/Model/split_bill.dart';
import 'package:intl/intl.dart';
import 'package:smartsplit_link/Presentation/attach_payment_page.dart';

class SplitResultPage extends StatefulWidget {
  const SplitResultPage(this.splitBill, {super.key});

  final SplitBill splitBill;

  @override
  State<SplitResultPage> createState() => _SplitResultPage2State();
}

class _SplitResultPage2State extends State<SplitResultPage> {
  late SplitBill _splitBill;
  Map<Friend, List<Map<ReceiptItem, int>>> splits = {};
  List<Friend> splitKeys = [];
  int overallTotal = 0;

  @override
  void initState() {
    super.initState();
    _splitBill = widget.splitBill;
    _generateSplits(_splitBill.receipt);
    splitKeys = splits.keys.toList();
  }

  List<Widget> _getAllSplits(List<Friend> friend) {
    List<Widget> splitPerPerson = [];

    for (Friend f in friend) {
      splitPerPerson.add(_constructSplit(f));
    }

    return splitPerPerson;
  }

  String _truncateName(String name, int maxLength) {
    if (name.length <= maxLength) return name;
    return '${name.substring(0, maxLength - 1)}…';
  }

  Widget _constructSplit(Friend f) {
    int total = 0;

    List<Map<ReceiptItem, int>> receiptItemMapList = splits[f]!;
    List<Map<String, String>> purchasedItemsDataMap = [];
    for (Map<ReceiptItem, int> receiptItemMap in receiptItemMapList) {
      List<ReceiptItem> receiptItemMapKeys = receiptItemMap.keys.toList();
      for (var receiptItemMapKey in receiptItemMapKeys) {
        if (receiptItemMap[receiptItemMapKey]! > 0) {
          purchasedItemsDataMap.add({
            'itemName': receiptItemMapKey.itemName,
            'quantity': receiptItemMap[receiptItemMapKey].toString(),
            'totalPrice': ((receiptItemMapKey.totalPrice /
                        receiptItemMapKey.quantity) *
                    receiptItemMap[receiptItemMapKey]! /
                    100)
                .toStringAsFixed(2),
          });
        }
        total +=
            (receiptItemMap[receiptItemMapKey]! *
                    receiptItemMapKey.totalPrice /
                    receiptItemMapKey.quantity)
                .floor();
      }
    }

    int tax = total;

    total =
        total +
        (total * _splitBill.receipt.additionalChargesPercent / 100).floor();

    tax = total - tax;

    overallTotal += total;

    return Padding(
      padding: const EdgeInsets.only(bottom: 10.0),
      child: Container(
        color: Colors.white,
        child: Column(
          children: [
            Container(
              decoration: BoxDecoration(
                border: Border(
                  bottom: BorderSide(
                    color: Theme.of(context).primaryColor,
                    width: 1.0,
                  ),
                ),
              ),
              child: Row(
                children: [
                  Padding(
                    padding: const EdgeInsets.fromLTRB(30.0, 15, 15, 15),
                    child: Container(
                      width: 80,
                      height: 80,
                      clipBehavior: Clip.antiAlias,
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        color:
                            Theme.of(
                              context,
                            ).colorScheme.surfaceContainerHighest,
                      ),
                      child: f.getProfilePicture(10),
                    ),
                  ),
                  Expanded(
                    child: Padding(
                      padding: const EdgeInsets.only(left: 30.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          if (f is GuestFriend)
                            Opacity(
                              opacity: 0.4,
                              child: Text(
                                "(Guest)",
                                style: TextStyle(fontSize: 12),
                              ),
                            ),
                          Text(
                            "RM ${(total / 100)}",
                            style: TextStyle(
                              fontWeight: FontWeight.bold,
                              fontSize: 20,
                            ),
                          ),
                          Text(
                            "${_truncateName(f.getName(), 18)}'s total",
                            maxLines: 1,
                          ),
                          _getPaymentAction(f),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),
            SizedBox(height: 30),
            for (Map<String, String> itemMap in purchasedItemsDataMap)
              Padding(
                padding: const EdgeInsets.symmetric(
                  vertical: 4.0,
                  horizontal: 15,
                ),
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Expanded(flex: 2, child: Text(itemMap["itemName"]!)),
                    Expanded(
                      flex: 1,
                      child: Text(
                        "x${itemMap["quantity"]!}",
                        textAlign: TextAlign.right,
                      ),
                    ),
                    Expanded(
                      flex: 1,
                      child: Text(
                        "RM${(itemMap["totalPrice"]!)}",
                        textAlign: TextAlign.right,
                      ),
                    ),
                  ],
                ),
              ),
            SizedBox(height: 20),
            Padding(
              padding: const EdgeInsets.symmetric(
                vertical: 4.0,
                horizontal: 15,
              ),
              child: Row(
                children: [
                  Expanded(flex: 2, child: Text("Extra charges")),
                  Expanded(
                    flex: 1,
                    child: Text(
                      "${_splitBill.receipt.additionalChargesPercent}%",
                      textAlign: TextAlign.right,
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: Text("RM${tax / 100}", textAlign: TextAlign.right),
                  ),
                ],
              ),
            ),
            SizedBox(height: 20),
          ],
        ),
      ),
    );
  }

  Widget _getPaymentAction(Friend targetFriend) {
    FriendPayment? payment;

    if (targetFriend is RegisteredFriend) {
      payment = _splitBill.members.firstWhere(
        (p) =>
            p.friend is RegisteredFriend &&
            (p.friend as RegisteredFriend).id == targetFriend.id,
      );
    } else if (targetFriend is GuestFriend) {
      payment = _splitBill.members.firstWhere(
        (p) =>
            p.friend is GuestFriend &&
            (p.friend as GuestFriend).getName() == targetFriend.getName(),
      );
    }

    if (payment != null) {
      if (payment.friend is RegisteredFriend && payment.hasPaid) {
        if ((payment.friend as RegisteredFriend).id == _splitBill.creatorId) {
          return Text(
            "Paid",
            style: TextStyle(color: Colors.green, fontWeight: FontWeight.bold),
          );
        }
        return Row(
          children: [
            Text("Paid", style: TextStyle(color: Colors.green, fontWeight: FontWeight.bold),),
              Text(" - "),
            GestureDetector(
              onTap: () {
                Navigator.of(context).push<SplitBill>(
                  PageRouteBuilder(
                    pageBuilder:
                        (_, __, ___) => AttachPaymentPage(
                          _splitBill,
                          (payment!.friend as RegisteredFriend),
                        ),
                    transitionDuration: Duration.zero,
                    reverseTransitionDuration: Duration.zero,
                  ),
                );
              },
              child: Text(
                "View Statement",
                style: TextStyle(color: Colors.blue),
              ),
            ),
          ],
        );
      } else if (payment.friend is RegisteredFriend && !payment.hasPaid) {
        return Text("Unpaid", style: TextStyle(color: Colors.red));
      } else if (payment.friend is GuestFriend && payment.hasPaid) {
        return Row(
          children: [
            Text("Paid", style: TextStyle(color: Colors.green),),
            Text(" - "),
            GestureDetector(
              onTap: () {
                Navigator.of(context).push<SplitBill>(
                  PageRouteBuilder(
                    pageBuilder:
                        (_, __, ___) =>
                            AttachPaymentPage(_splitBill, payment!.friend),
                    transitionDuration: Duration.zero,
                    reverseTransitionDuration: Duration.zero,
                  ),
                );
              },
              child: Text(
                "View Statement",
                style: TextStyle(color: Colors.blue),
              ),
            ),
          ],
        );
      } else if (payment.friend is GuestFriend && !payment.hasPaid) {
        return Row(
            children: [
              Text("Unpaid", style: TextStyle(color: Colors.red, fontWeight: FontWeight.bold),),
              Text(" - "),
              GestureDetector(
                onTap: () async {
                  final updatedBill = await Navigator.of(
                    context,
                  ).push<SplitBill>(
                    PageRouteBuilder(
                      pageBuilder:
                          (_, __, ___) => AttachPaymentPage(
                            _splitBill,
                            (payment!.friend as GuestFriend),
                          ),
                      transitionDuration: Duration.zero,
                      reverseTransitionDuration: Duration.zero,
                    ),
                  );

                  if (updatedBill != null) {
                    setState(() {
                      _splitBill = updatedBill;
                      _generateSplits(updatedBill.receipt);
                      splitKeys = splits.keys.toList();
                    });
                  }
                },
                child: Text(
                  "Attach Payment",
                  style: TextStyle(color: Colors.blue),
                ),
              ),
            ],
          );
      }
    }

    return SizedBox(height: 0);
  }

  void _generateSplits(Receipt receipt) {
    List<Friend> friends = [];

    for (List<FriendSplit> friendSplitsPerItem in receipt.friendSplits) {
      for (FriendSplit friendSplit in friendSplitsPerItem) {
        if (!friends.contains(friendSplit.friend)) {
          friends.add(friendSplit.friend);
        }
      }
    }

    for (Friend f in friends) {
      List<Map<ReceiptItem, int>> splitPerFriend = [];
      for (int i = 0; i < receipt.receiptItems.length; i++) {
        for (int j = 0; j < receipt.friendSplits[i].length; j++) {
          if (receipt.friendSplits[i][j].friend == f) {
            splitPerFriend.add({
              receipt.receiptItems[i]: receipt.friendSplits[i][j].quantity,
            });
            break;
          }
        }
        splits.addAll({f: splitPerFriend});
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    overallTotal = 0;
    return SafeArea(
      child: Scaffold(
        body: ListView(
          children: [
            SizedBox(height: 40),
            Padding(
              padding: const EdgeInsets.only(left: 10),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    _splitBill.receipt.title,
                    style: TextStyle(fontWeight: FontWeight.bold, fontSize: 30),
                  ),
                  Text(
                    DateFormat(
                      'yyyy-MM-dd HH:mm:ss',
                    ).format(_splitBill.receipt.now),
                  ),
                ],
              ),
            ),
            SizedBox(height: 50),
            Column(children: _getAllSplits(splitKeys)),
            SizedBox(height: 50),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 10.0),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    "Overall total",
                    style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                  ),
                  Text(
                    "RM ${(overallTotal + _splitBill.receipt.roundingAdjustment) / 100}",
                  ),
                ],
              ),
            ),
            SizedBox(height: 20),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 10.0),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text("Rounding adjustment", style: TextStyle(fontSize: 12)),
                  Text(
                    (_splitBill.receipt.roundingAdjustment > 0 ? "+" : "") +
                        (_splitBill.receipt.roundingAdjustment / 100)
                            .toString(),
                  ),
                ],
              ),
            ),
            SizedBox(height: 80),
          ],
        ),
      ),
    );
  }
}
