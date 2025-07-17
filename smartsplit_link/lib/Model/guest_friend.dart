import 'package:avatar_plus/avatar_plus.dart';
import 'package:flutter/material.dart';
import 'package:smartsplit_link/Model/Friend.dart';

class GuestFriend implements Friend{
  String name;
  GuestFriend(this.name);
  
  @override
  String getName() {
    return name;
  }

  @override
  Widget getProfilePicture(double width) {
    return AvatarPlus(
          name,
          width: width,
        );
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is GuestFriend && runtimeType == other.runtimeType && name == other.name;

  @override
  int get hashCode => name.hashCode;

}