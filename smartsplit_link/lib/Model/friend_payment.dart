import 'package:smartsplit_link/Model/Friend.dart';

class FriendPayment {
  final Friend friend;
  final int totalDebt;
  final bool hasPaid;
  final String? paymentImageLink;
  final DateTime? paidAt;

  FriendPayment({
    required this.friend,
    required this.totalDebt,
    required this.hasPaid,
    this.paymentImageLink,
    this.paidAt,
  });
}
