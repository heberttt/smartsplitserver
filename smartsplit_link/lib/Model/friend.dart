import 'package:flutter/material.dart';

abstract class Friend {
  Widget getProfilePicture(double width);
  String getName();
}