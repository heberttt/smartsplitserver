import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:flutter_web_plugins/url_strategy.dart'; 
import 'package:smartsplit_link/Model/split_bill.dart';
import 'package:smartsplit_link/Presentation/error_page.dart';
import 'package:smartsplit_link/Presentation/split_result_page.dart';
import 'package:smartsplit_link/Service/split_service.dart';


final _router = GoRouter(
  routes: [
    GoRoute(
      path: '/splitview',
      builder: (context, state) {
        final billIdStr = state.uri.queryParameters['billId'];
        final token = state.uri.queryParameters['token'];

        if (billIdStr == null || token == null) {
          return const ErrorPage("Missing billId or token");
        }

        final billId = int.tryParse(billIdStr);
        if (billId == null) {
          return const ErrorPage("Invalid billId");
        }

        return FutureBuilder<SplitBill>(
          future: SplitService().getMySplitBill(billId, token),
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Scaffold(
                body: Center(child: CircularProgressIndicator()),
              );
            } else if (snapshot.hasError) {
              if (snapshot.error is UnauthorizedException) {
                return const ErrorPage("Wrong Token: Please check your link or try again.");
              } else {
                return const ErrorPage("Something went wrong!");
              }
            } else if (!snapshot.hasData) {
              return const ErrorPage("No data found");
            }

            return SplitResultPage(snapshot.data!);
          },
        );
      },
    ),
  ],
  errorBuilder: (context, state) {
    return const ErrorPage("Page Not Found (404)");
  },
);

void main() {
  usePathUrlStrategy();

  runApp(MaterialApp.router(
    routerConfig: _router,
  ));
}