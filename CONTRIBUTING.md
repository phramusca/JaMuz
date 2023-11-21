# Contributing

By contributing to this project you agree to license your contribution under the terms of the [GNU GPLv3](LICENSE).

[[_TOC_]]

## Issues

[Open an issue](https://github.com/phramusca/JaMuz/issues?state=open) for anything you would like to see in JaMuz, but please check other issues first.

## Internationalization

Using [weblate.org](https://hosted.weblate.org/engage/jamuz/)

**Warning! if you consider manual edition (ie: without weblate):**

- The **bundle\*\*.properties** files are **ISO 8859-1** encoded.

  - This means: NO UTF-8 nor anything else!

  - Using a different encoding may certainly result in weird characters in the GUI

- NetBeans respect the convention, but other editors may use instead UTF-8 for instance

## Pull Requests

Pull requests are welcome.
Please submit to the `master` branch.

### Get Started

- Clone repository to {Repo}
- [Get a TheMovieDb API key for free](https://www.themoviedb.org/faq/api)
- [Get a LastFm API key for free](http://www.last.fm/api/account/create)
- [Get an AcoustId API key for free](https://acoustid.org/)
- Create/update keys.properties file in {Repo}/res

  ```text
  TheMovieDb={yourKey}
  LastFm={yourKey}
  AcoustId={yourKey}
  ```

- Create JaMuz.db by running:
  - on Windows: `database\create_db.bat`.
    - (If sqlite3 is not installed, script will install it using Chocolatey, but you need to "Run as administrator")
  - on Linux: `database/create_db.sh`
- Create folder {Repo}/target-data and copy there your files from:
  - database/JaMuz.db
  - dist-data/JaMuz.properties

> ! WARNING ! Each time you will do a `mvn clean`, files in {Repo}/target will be replaced with those!

- Open project using [NetBeans](https://netbeans.org/downloads/)
- Build project
- You can now run and enjoy (hopefully)

### Stat sources

In addition to the currently supported stat sources (Guayadeque, Kodi, Media Monkey, Mixxx, MyTunes) you can add one simply by extending the jamuz.StatSourceAbstract class and adding entry to database.

## Release process

1. If `database/JaMuz_creation.sql` has changed:

    - Create a file `data/system/sql/`$version`.sql`.
      - `$version` is the new database version.
    - Update `Jamuz.java`, set new version in `getDb().updateSchema(`$version`)` call.
    - Update `database/JaMuz_insertion_minimal.sql` to set an entry with $version.
    - Check that JaMuz properly handles update.
      - Export prod schema and compare with JaMuz_creation.sql.

1. Check changes to release content:
  
    - Create a new local release candidate with [local self-hosted runner](#github-self-hosted-runner)
      - [Start job](https://github.com/phramusca/JaMuz/actions/workflows/maven_local.yml)
      - Get `dist` folder in ~/actions-runner/_work/JaMuz/JaMuz/
    - Compare it to [previous release](https://github.com/phramusca/JaMuz/releases)
    - Create the `target-data/data/system/update/`$previousVersion`--`x.y.z`.csv` file for updating from $previousVersion to x.y.z (the one being released). Example:

      ```csv
      //action,fileOrDirectory
      rm,doc/LISEZMOI.TXT
      rm,doc/README.TXT
      rm,lib
      cpo,data/icon/genre
      cp,Slsk.properties
      cpo,JaMuz.jar
      ```

      - action can be:
        - `rm` to remove a file or directory.
        - `cp` to copy a file or directory only if it doesn't exist.
        - `cpo` to either:
          - copy a file, overwriting it if it already exists.
          - recursively merge directory to destination, with the source taking precedence.

1. Update pom.xml (remove "-dev" suffix)

    ```xml
    <version>x.y.z</version>
    ```

1. Update CHANGELOG.md

1. Tag last commit "vx.y.z" and push. This will trigger the [release github action](https://github.com/phramusca/JaMuz/actions/workflows/maven.yml).

1. Check [created release](https://github.com/phramusca/JaMuz/releases).

1. Update pom.xml

    ```xml
    <version>x.y.z+1-dev</version>
    ```

1. Commit (named vx.y.z-dev) & PUSH

1. Edit [created release](https://github.com/phramusca/JaMuz/releases) and "Publish release".

1. [Edit gh-pages](https://github.com/phramusca/JaMuz/edit/gh-pages/index.md) with link to new release.

### Github self-hosted runner

- [Create a new self-hosted runner](https://github.com/phramusca/JaMuz/settings/actions/runners/new) on WSL or directly on linux

- Install dependencies:
  - (If required): Install Custom SSL Certificate

    ```bash
    sudo cp /mnt/c/IT/netskope/rootcacert.pem /usr/local/share/ca-certificates/
    sudo update-ca-certificates
    ```

  - Install maven

    ```bash
    sudo apt install maven
    ```

  - Install sqlite3

    ```bash
    sudo apt install sqlite3
    ```

- Start the runner

  ```bash
  ./run.sh
  ```

## Roadmap to release v1.0.0

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
