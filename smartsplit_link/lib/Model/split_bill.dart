import 'package:smartsplit_link/Model/Friend.dart';
import 'package:smartsplit_link/Model/friend_payment.dart';
import 'package:smartsplit_link/Model/friend_split.dart';
import 'package:smartsplit_link/Model/guest_friend.dart';
import 'package:smartsplit_link/Model/receipt.dart';
import 'package:smartsplit_link/Model/receipt_item.dart';
import 'package:smartsplit_link/Model/registered_friend.dart';

class SplitBill {
  final int id;
  final String creatorId;
  final Receipt receipt;
  final List<FriendPayment> members;
  final String publicAccessToken;

  SplitBill({
    required this.id,
    required this.creatorId,
    required this.receipt,
    required this.members,
    required this.publicAccessToken,
  });

  factory SplitBill.fromJson(Map<String, dynamic> json) {
    final int id = json['id'];
    final receiptJson = json['receipt'];
    final creatorId = json['creatorId'];
    final membersJson = json['members'];
    final publicAccessToken = json['publicAccessToken'] ?? '';

    String title = receiptJson['name'] ?? "Untitled Split";
    int additionalCharges = receiptJson['additionalChargesPercent'] ?? 0;
    int rounding = receiptJson['roundingAdjustment'] ?? 0;
    DateTime now = DateTime.parse(receiptJson['now']);

    List<ReceiptItem> items = [];
    List<List<FriendSplit>> allFriendSplits = [];

    for (var item in receiptJson['splits']) {
      items.add(
        ReceiptItem(
          itemName: item['itemName'],
          totalPrice: item['totalPrice'],
          quantity: item['quantity'],
        ),
      );

      List<FriendSplit> splits = [];
      for (var fs in item['friendSplits']) {
        var f = fs['friend'];
        Friend friend;

        if (f['id'] != null) {
          friend = RegisteredFriend(f['id'], "", "", "");
        } else {
          friend = GuestFriend(f['username']);
        }

        splits.add(FriendSplit(friend, fs['quantity']));
      }

      allFriendSplits.add(splits);
    }

    Receipt receipt = Receipt(
      title: title,
      receiptItems: items,
      friendSplits: allFriendSplits,
      additionalChargesPercent: additionalCharges,
      roundingAdjustment: rounding,
    )..now = now;

    List<FriendPayment> members = [];
    for (var m in membersJson) {
      final f = m['friend'];
      Friend friend;

      if (f['id'] != null) {
        friend = RegisteredFriend(f['id'], "", "", "");
      } else {
        friend = GuestFriend(f['username']);
      }

      members.add(
        FriendPayment(
          friend: friend,
          totalDebt: m['totalDebt'],
          hasPaid: m['hasPaid'],
          paymentImageLink: m['paymentImageLink'],
          paidAt: m['paidAt'] != null ? DateTime.parse(m['paidAt']) : null,
        ),
      );
    }

    return SplitBill(
      id: id,
      creatorId: creatorId,
      receipt: receipt,
      members: members,
      publicAccessToken: publicAccessToken,
    );
  }
}