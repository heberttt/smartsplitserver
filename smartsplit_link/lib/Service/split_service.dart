import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:smartsplit_link/Model/friend.dart';
import 'package:smartsplit_link/Model/friend_payment.dart';
import 'package:smartsplit_link/Model/friend_split.dart';
import 'package:smartsplit_link/Model/registered_friend.dart';
import 'package:smartsplit_link/Model/split_bill.dart';
import 'package:http/http.dart' as http;
import 'package:smartsplit_link/backend_url.dart';
import 'package:path/path.dart' as path;

class UnauthorizedException implements Exception {
  final String message;
  UnauthorizedException(this.message);

  @override
  String toString() => 'UnauthorizedException: $message';
}

class SplitService {


  

  SplitBill enrichSplitBill(SplitBill bill, List<RegisteredFriend> myFriends) {
    Friend enrichFriend(Friend friend) {
      if (friend is RegisteredFriend) {
        final match = myFriends.firstWhere(
          (f) => f.id == (friend).id,
          orElse: () => friend,
        );

        return RegisteredFriend(
                  match.id,
                  match.email,
                  match.username,
                  match.profilePictureLink,
                )
                as Friend;
      }
      return friend;
    }

    for (int i = 0; i < bill.receipt.friendSplits.length; i++) {
      for (int j = 0; j < bill.receipt.friendSplits[i].length; j++) {
        final original = bill.receipt.friendSplits[i][j];
        bill.receipt.friendSplits[i][j] = FriendSplit(
          enrichFriend(original.friend),
          original.quantity,
        );
      }
    }

    for (int i = 0; i < bill.members.length; i++) {
      final original = bill.members[i];
      bill.members[i] = FriendPayment(
        friend: enrichFriend(original.friend),
        totalDebt: original.totalDebt,
        hasPaid: original.hasPaid,
        paymentImageLink: original.paymentImageLink,
        paidAt: original.paidAt,
      );
    }

    return bill;
  }

  Future<SplitBill> getMySplitBill(int billId, String token) async {
    final response = await http.get(
      Uri.parse("${BackendUrl.GATEWAY_URL}/splitbill?billId=$billId&token=$token"),
      headers: {
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode == 200) {
      final jsonMap = jsonDecode(response.body);
      final Map<String, dynamic> data = jsonMap['data'];

      final SplitBill bill = SplitBill.fromJson(data);

      final Set<String> friendIds = {};

      for (final member in bill.members) {
        if (member.friend is RegisteredFriend) {
          friendIds.add((member.friend as RegisteredFriend).id);
        }
      }

      for (final itemSplits in bill.receipt.friendSplits) {
        for (final split in itemSplits) {
          if (split.friend is RegisteredFriend) {
            friendIds.add((split.friend as RegisteredFriend).id);
          }
        }
      }

      final List<RegisteredFriend> myFriends = [];
      for (final id in friendIds) {
        try {
          final friend = await getMyFriend(id);
          myFriends.add(friend);
        } catch (e) {
          print("Failed to fetch friend with id $id: $e");
        }
      }

      return enrichSplitBill(bill, myFriends);
    } else if (response.statusCode == 401) {
      throw UnauthorizedException('Wrong token provided');
    } else {
      throw Exception('Failed to load split bill: ${response.statusCode}');
    }
  }

  Future<RegisteredFriend> getMyFriend(String accountId) async {
    final response = await http.get(
      Uri.parse("${BackendUrl.GATEWAY_URL}/account2?id=$accountId"),
      headers: {
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode == 200) {
      final Map<String, dynamic> json = jsonDecode(response.body);
      final data = json['data'];

      return RegisteredFriend.fromJson(data);
    } else {
      throw Exception('Failed to fetch friend: ${response.statusCode}');
    }
  }


  Future<bool> uploadPayment(
    int billId,
    String token,
    String imageFilePath,
    String guestName,
  ) async {
    try {
      final uri = Uri.parse("${BackendUrl.GATEWAY_URL}/splitbill");

      var request = http.MultipartRequest('POST', uri);

      request.fields['billId'] = billId.toString();
      request.fields['token'] = token;
      request.fields['guestName'] = guestName;

      final response = await http.get(Uri.parse(imageFilePath));
      if (response.statusCode == 200) {
        request.files.add(http.MultipartFile.fromBytes(
          'file',
          response.bodyBytes,
          filename: path.basename(imageFilePath),
        ));
      } else {
        print('Failed to fetch image bytes from blob URL: ${response.statusCode}');
        return false;
      }

      final streamedResponse = await request.send();
      final responseBody = await streamedResponse.stream.bytesToString();

      if (streamedResponse.statusCode == 200) {
        print('Payment uploaded successfully. Response: $responseBody');
        return true;
      } else {
        print('Failed to upload payment. Status: ${streamedResponse.statusCode}, Body: $responseBody');
        return false;
      }
    } catch (e) {
      print('Error during payment upload: $e');
      return false;
    }
  }
}