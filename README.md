# frontend-template-provider

[![Build Status](https://travis-ci.org/hmrc/frontend-template-provider.svg)](https://travis-ci.org/hmrc/frontend-template-provider) [ ![Download](https://api.bintray.com/packages/hmrc/releases/frontend-template-provider/images/download.svg) ](https://bintray.com/hmrc/releases/frontend-template-provider/_latestVersion)

A mustache template provider that gives you a govuk template. It allows for variables to be passed into the template that then populates HTML elements in an idiomatic fashion. Also, you'll find the most up-to date template and elements. More to come!

To add this to your service, place this inside your routes for `Dev` and `Prod` in application.conf:

```json &lt;!--even though this isn&#39;t json, just gives it a nicer colour--&gt;
frontend-template-provider {
 host = localhost
 port = 9310
}
```

This service is currently available when using service-manager only.

## How to use the frontend-template-provider

The easiest way to use it in a scala application is to have the templates served to and rendered by the [local-template-renderer](<url for the template renderer>) library. Latest details on how to use and set up the library are provided in it's own README. For the purpose of this README, we are assuming that you have crated an instance called 	`localTemplateRenderer` (much like in the tests of this project) and we are illustrating how to use the library that way. If you are not using a dependency injected instance, you most likely would have a global instance (upper camel-case rather than lower camel-case).

### Populating the main tag inside the body

If you only want to populate the main content of the page and wish to use sensible defaults everywhere else, you can simply just pass in the `article` and you are good to go:

```scala
val article = Html("<p>hello world!</p>")
localTemplateRenderer.parseTemplate(article, Map())
```

*Specifying a version of assets-fronted*

The version of assets-frontend defaults to the version set by the template. This will almost always be kept as the latest. **Warning:** This could break your frontend on page reload if a version of assets-fronted is released with a breaking change that changes or removes styles for some markup you were relying on.

In order to avoid this issue, frontends can specify which version of assets-frontend they rely on by passing through a path to which version of assets-frontend you require:

```scala
localTemplateRenderer.parseTemplate(article, Map(
  "assetsPath" -> assetsPath
))
```

*Adding extra html around the article*

The main `content` of the page are populated using the following mustache:

```html
<div id="content">

 {{{ actingAttorneyBanner }}}

 {{{ mainContentHeader }}}

 {{{ article }}}

 {{{ sidebar }}}

 {{{ getHelpForm }}}

</div>
```

`article` is the most important part of the page. It is what your application is responsible for and therefore is the only part of the page that is mandated. Everything else is optional and must be passed into the `templateArgs` (the map that is the second passed variable into the `parseTemplate` function.

So, for instance, if you want to add a sidebar with your article, if call the `localTemplateRenderer` in the following way:

```scala
val article = Html("<p>hello world!</p>")
val sidebars = Html("<p class="sidebar">sidebar</p>")
localTemplateRenderer.parseTemplate(article, Map(
  "sidebar" -> sidebar
))
```
the main content of the page would be rendered as follows:

```html
<div id="content">
  <p>hello world!</p>
  <p class="sidebar">sidebar</p>
</div>
```

There are two points to note here:

- The mustache template respects the ordering of the page.
- If you do not wish to populate a part of the page, you do need to pass anything for it. Sensible defualts will be given, and in the case of the sidebar, the default is to not populate this part of the template.

*Adding a class to the main element*

~~~scala
localTemplateRenderer.parseTemplate(Html(""), Map(
        "mainClass" -> "mainClass"
      ))
~~~

would give 

```html
<main id="wrapper" role="main" class="mainClass" >
```

*Adding attributes to the main element*

```scala
localTemplateRenderer.parseTemplate(Html(""), Map(
        "mainAttributes" -> """ id="mainID" """
      ))
```

would give 

```html
<main id="wrapper" role="main" id="mainID" >
```

*note, neither `mainAttributes` nor `mainClass` have default values should they not be provided.

*Adding a beta banner at the top of your main element*

There are two beta banner variations supported. 

1 - Providing a beta banner without the feedback URL

Simply having:

~~~scala
localTemplateRenderer.parseTemplate(Html(""), Map(
        "betaBanner" -> true
      ))
~~~

Would produce the following beta banner:

```html
<div class="beta-banner">
  <p>
     <strong class="phase-tag">BETA</strong>
     <span>This is a new service.</span>
  </p>
</div>
```

2 - Providing the feedback identifier for your product, you would render a feedback link.

```scala
localTemplateRenderer.parseTemplate(Html(""), Map(
        "betaBanner" -> Map("feedbackIdentifier" -> "PTA")
      ))
```

Would produce the following:

```html
<div class="beta-banner">
  <p>
     <strong class="phase-tag">BETA</strong>
     <span>This is a new service - your <a id="feedback-link"
                   href="beta-feedback-unauthenticated?service=PTA"
                   data-sso="false"
                   data-journey-click="other-global:Click:Feedback">feedback</a> will help us to improve it.</span>
  </p>
</div>
```

By default, no BETA banner is shown.

*Adding a service-info to the page*

The service-info is traditionally the part of the main element where one populates the session information of the user. If you do not provide any for the service-info, the default is to have an empty service info `div`. 

The `service-info` follows the same pattern as the `play-ui`'s way.

The options of what you can populate are as follows:

- You can allow `grid-wrapper` to be one of the classes of the `service-info` by having `includeGridWrapper -> true` as an option in the `templateArgs`. By default it's not set.


Example of a fully populated service-info:

```scala
localTemplateRenderer.parseTemplate(Html(""), Map(
   "includeGridWrapper" -> true
))
```

would produce:

```html
<div class="service-info grid-wrapper">
    <div class="logo">
        <span class="organisation-logo organisation-logo-medium">HM Revenue &amp; Customs</span>
    </div>
    <div id="lastlogin" class="service-info__help">
        <p>
            Dave, you last signed in 01 Jan 2017.
            <br><a id="logOutStatusHref" href="www.example.com/logout">Sign out</a>
        </p>
    </div>
</div>
```

### Populating the head

*Adding optimizely for AB testing*

If you wish to use optimizely, you must look at the [snippet you get from optimizely](https://help.optimizely.com/Set_Up_Optimizely/Implement_the_snippet_for_Optimizely_Classic) and check for the url and project ID.

Providing the optimizely base url and project id as follows:

```scala
localTemplateRenderer.parseTemplate(Html(""), Map(
	"optimizelyBaseUrl" -> "//cdn.optimizely.com/",
	"optimizelyProjectId" -> "id123"
)).body
```

```html
<script src='//cdn.optimizely.com/js/id123.js' type='text/javascript'></script>
```

NOTE: if you do not provide `optimizelyBaseUrl`, the default will be `//cdn.optimizely.com/`. If you do not provide the `optimizelyProjectId`, the snippet will not be generated.

*Adding a page title*

By default, the page title of the page is set to `<title> GOV.UK - The best place to find government services and information </title>`. However, if you want to change that, and you should, you have to add a value for `pageTitle` in the `templateArgs`:

```scala
localTemplateRenderer.parseTemplate(Html(""), Map(
	"pageTitle" -> "My Title"
))
```

produces:

```html
<title> My title </title>
```

*Adding link elements to the head*


The template only supports adding `sylesheets` link elements to the head. You must provide the url for them and optionally you can specify if the media type is a print. The default is no media type which is also the same as the media type `screen`. 

```scala
localTemplateRenderer.parseTemplate(Html(""), Map(
	"linkElems" -> List(
	      Map("url" -> "www.example.com/some.css"),
	      Map("url" -> "www.example.com/other.css", "print" -> true)
	))
)
```

will produce:

```html
<link rel="stylesheet" type="text/css" href="www.example.com/some.css" />
<link rel="stylesheet" type="text/css" href="www.example.com/other.css" media="print"/>
```

*Adding navigation*

You can add a navigation to the head of the page. You can provide the one navigation title that can optionally have a navigation link with it. You can also provide navigation links that are populated inside a menu.

```scala
localTemplateRenderer.parseTemplate(Html(""), Map(
	"navTitle" -> "My Title",
	"navTitleLink" -> "www.example.com"
	"hasNavLinks" -> true,
	"navLinks" -> Seq(
		Map("url" -> "url1.com", "text" -> "link title"),
		Map("url" -> "url2.com", "text" -> "second link title")
	)
))
```

would produce:

```html
<div class="header-proposition">
    <div class="content">
        <a href="#proposition-links" class="js-header-toggle menu">Menu</a>
        <nav id="proposition-menu" class="header__menu" role="navigation">
            <a href="www.example.com" class="header__menu__proposition-name"> My Title </a>
            <ul id="proposition-links" class="header__menu__proposition-links">
	            <li><a href="url1.com">link title</a></li>
	            <li><a href="url2.com">second link title</a></li>
            </ul>
        </nav>
    </div>
</div>
```

Note: Due to the mustache being logically, it assumes that if you have a sequence for a key that anything wrapped inside the a conditional for the key will be rendered the same amount of time as the sequence length. Therefore, `hasNavLinks` is essential and must be provided if you want to rendered `navLinks`.

### Populating the footer

*Adding additional footer links*

The template now provides the main footer links. There is no need to add the footer links from `play-ui`.

You can add additional footerlinks.

```scala
localTemplateRenderer.parseTemplate(Html(""), Map(
	"additionalFooterLinks" -> Seq(
	  Map("url" -> "service details", "text" -> "www.example.com/my-service-details"),
	  Map("url" -> "service help", "text" -> "www.example.com/my-service-help")
	)
))						
```

would produce the following additional footer links:

```html
<!--template footer links-->
<li><a href='/help/cookies' target="_blank" data-sso="false" data-journey-click="footer:Click:Cookies">Cookies</a></li>
                    <li><a href='/help/privacy' target="_blank" data-sso="false" data-journey-click="footer:Click:Privacy policy">Privacy policy</a></li>
                    <li><a href='/help/terms-and-conditions' target="_blank" data-sso="false" data-journey-click="footer:Click:Terms and conditions">Terms and conditions</a></li>
                    <li><a href='https://www.gov.uk/help' target="_blank" data-sso="false" data-journey-click="footer:Click:Help">Help using GOV.UK</a></li>
<!--additional footer links-->
<li><a href="www.example.com/my-service-details">service details</a></li>
<li><a href="www.example.com/my-service-help">service help</a></li>
```

*Adding additional script elems*

You can add your own script elements. The following example:

```scala
localTemplateRenderer.parseTemplate(Html(""), Map(
	"scriptElems" -> Seq(
	  Map("url" -> "www.example.com/script.js"),
	  Map("url" -> "www.example.com/second.js")
	)
))
```

would produce the following script elements in the footer:

```html
<script src="www.example.com/script.js" type="text/javascript"></script>
<script src="www.example.com/second.js" type="text/javascript"></script>
```

*Adding your SSO url*

You can add the `ssoUrl` in the footer similar to the following example:

```scala
localTemplateRenderer.parseTemplate(Html(""), Map(
  "ssoUrl" -> "www.example.com/sso"
)).body
```

that produces:

```html
<script type="text/javascript">var ssoUrl = www.example.com/sso;</script>
```
## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")
