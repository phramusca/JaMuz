# Contributing

## Issues

[Open an issue](https://github.com/phramusca/JaMuz/issues?state=open) for anything you would like to see in JaMuz, but please check other issues first.

## Pull Requests

Pull requests are welcome.
Please submit to the `master` branch.

### Roadmap to Release version 1.0

- Test Plan:
  - add unit tests
  - review/update functional tests
- Do all FIX-MEs 
  - (to be moved or copied to GitHub Issues)
- Initial options setup interface
- Code review:
  - Exceptions handling
  - Internationalization
  - Files headers (license)
  - Javadoc

### Internationalization

Using [weblate.org](https://hosted.weblate.org/engage/jamuz/)

- If you plan to update the bundle**.properties files, please remember that such files are ISO 8859-1 encoded.
- Using a different encoding may certainly result in weird characters in the GUI
- ie: NO UTF-8 nor anything else
(NetBeans respect the convention, but other editors may use instead UTF-8 for instance, the most common default encoding)

### Stat sources

In addition to the currently suported stat sources (Guayadeque, Kodi, Media Monkey, Mixxx, MyTunes) you can add one simply by extending the jamuz.StatSourceAbstract class and adding entry to database.

## License

By contributing to this project you agree to license your contribution under the terms of the [GNU GPLv3](LICENSE).

