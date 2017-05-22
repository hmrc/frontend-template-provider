# frontend-template-provider

[![Build Status](https://travis-ci.org/hmrc/frontend-template-provider.svg)](https://travis-ci.org/hmrc/frontend-template-provider) [ ![Download](https://api.bintray.com/packages/hmrc/releases/frontend-template-provider/images/download.svg) ](https://bintray.com/hmrc/releases/frontend-template-provider/_latestVersion)

A mustache template provider that gives you a govuk template. More to come!

To add this to your service, place this inside your routes for `Dev` and `Prod` in application.conf:

```
frontend-template-provider {
 host = localhost
 port = 9310
}
```

This service is currently available when using service-manager only.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")
