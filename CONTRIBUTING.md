# Contributing

By contributing to this project you agree to license your contribution under the terms of the [GNU GPLv3](LICENSE).

## Issues

[Open an issue](https://github.com/phramusca/JaMuz/issues?state=open) for anything you would like to see in JaMuz, but please check other issues first.

## Internationalization

Using [weblate.org](https://hosted.weblate.org/engage/jamuz/)

- If you plan to update the bundle**.properties files, please remember that such files are ISO 8859-1 encoded.
  - Using a different encoding may certainly result in weird characters in the GUI
    - ie: NO UTF-8 nor anything else
    - NetBeans respect the convention, but other editors may use instead UTF-8 for instance
 
## Pull Requests

Pull requests are welcome.
Please submit to the `master` branch.

### Get Started

* Clone repository to {Repo}
* Open project using [NetBeans](https://netbeans.org/downloads/)
* Build project
* [Get a TheMovieDb API key for free](https://www.themoviedb.org/faq/api)
* [Get a LastFm API key for free](http://www.last.fm/api/account/create)
* Create a keys.properties in {Repo}/src/jamuz
  ```
  TheMovieDb={yourKey}
  LastFm={yourKey}
  ```
* Copy from {Repo}/dist to {Repo}/ :
  * JaMuz.db
  * JaMuz.properties
* You can now run and enjoy (hopefully)

### Stat sources

In addition to the currently suported stat sources (Guayadeque, Kodi, Media Monkey, Mixxx, MyTunes) you can add one simply by extending the jamuz.StatSourceAbstract class and adding entry to database.

### Roadmap to Release version 1.0

- Test Plan:
  - add unit tests
  - review/update functional tests
- Do all FIXMEs
- Update wiki, website, release notes and CHANGES.md
- Initial options setup interface
- Code review:
  - Exceptions handling
  - Internationalization
  - Files headers (license)
  - Javadoc
