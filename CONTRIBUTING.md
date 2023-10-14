# Contributing

By contributing to this project you agree to license your contribution under the terms of the [GNU GPLv3](LICENSE).

## Issues

[Open an issue](https://github.com/phramusca/JaMuz/issues?state=open) for anything you would like to see in JaMuz, but please check other issues first.

## Internationalization

Using [weblate.org](https://hosted.weblate.org/engage/jamuz/)

**Notes if you consider manual edition (ie: without weblate):**

- The **bundle\*\*.properties** files are **ISO 8859-1** encoded.

  - This means: NO UTF-8 nor anything else!

  - Using a different encoding may certainly result in weird characters in the GUI

- NetBeans respect the convention, but other editors may use instead UTF-8 for instance

## Pull Requests

Pull requests are welcome.
Please submit to the `master` branch.

### Get Started

- Clone repository to {Repo}
- Open project using [NetBeans](https://netbeans.org/downloads/)
- Build project
- [Get a TheMovieDb API key for free](https://www.themoviedb.org/faq/api)
- [Get a LastFm API key for free](http://www.last.fm/api/account/create)
- [Get an AcoustId API key for free](https://acoustid.org/)
- Create a keys.properties file in {Repo}/res

  ```text
  TheMovieDb={yourKey}
  LastFm={yourKey}
  AcoustId={yourKey}
  ```

- Copy from {Repo}/dist to {Repo}:
  - JaMuz.db
  - JaMuz.properties
- You can now run and enjoy (hopefully)

### Stat sources

In addition to the currently suported stat sources (Guayadeque, Kodi, Media Monkey, Mixxx, MyTunes) you can add one simply by extending the jamuz.StatSourceAbstract class and adding entry to database.

## Release process

1. If `database/JaMuz_creation.sql` has changed:

    - Create a file `data/system/sql/`$version`.sql`.
      - `$version` is the new database version.
    - Update `Jamuz.java`, set new version in `getDb().updateSchema(`$version`)` call.
    - Update `database/JaMuz_insertion_minimal.sql` to set an entry with $version.
    - Check that JaMuz properly handles update.
      - Export prod schema and compare with JaMuz_creation.sql.

1. Update pom.xml

    ```xml
    <version>x.y.z</version>
    ```

1. Update release notes

    - [French LISEZMOI.TXT](dist-data/doc/LISEZMOI.TXT)
    - [English README.TXT](dist-data/doc/README.TXT)

1. Build `dist` folder with NetBeans: `Run / Clean and build project (JaMuz)`

1. Install 7zip, if not already done

    ```bash
    sudo apt install p7zip-full
    ```

1. Create 7z release file

    ```bash
    sh ./release.sh 10.50.4056 ../../ReposSides/JaMuz/Backup_Releases/

    Usage:

      sh ./release.sh <version> <backup path>

    <version> must be of type x.y.z (ex: 0.5.41)
    <backup path> is the path to the releases backup folder
      Ex: 1.0.51
      Regex: ^[0-9]+\.[0-9]+\.[0-9]+$
    ```

1. **TEST**
1. Commit (named vx.y.z) & PUSH
1. [Create release on github](https://github.com/phramusca/JaMuz/releases/new) based on [previous ones](https://github.com/phramusca/JaMuz/releases)

    - Drop 7z built earlier
    - [Edit gh-pages](https://github.com/phramusca/JaMuz/edit/gh-pages/index.md) with link to new release

1. Update pom.xml

    ```xml
    <version>x.y.z+1-dev</version>
    ```

1. Commit (named vx.y.z-dev) & PUSH

## Roadmap to Release version 1.0

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
