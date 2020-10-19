# testalyser

> CLI utility which can process a given set of test runs and provides a human-readable report.

## Project structure

```
src
└─main
 │  └─java
 │    └─com
 │       └─tm
 │          └─testalyser
 │             └─cli           - containg the command line interface, entry point;
 │             └─implemenation - analyser, parser and loader implementations;
 │             └─template      - hostst the utility class that mimics a proper templating engine;
 │             └─model         - analyser related models;
 └─test
    └─java
      └─com
         └─tm
            └─testalyser       - acceptance tests
```

## Setup

#### Steps:

1. Unzip the archive that contains this README.md;
2. Run *./gradlew clean build* in the root directory of the application (where build.gradle file is);
3. From root repository folder navigate to *build/libs* folder

After the steps there should find a testalyser-1.0-SNAPSHOT.jar in the folder. In the current iteration the tool is 
simple enough to use (read: primitive).  Opening a command line in the folder and running the examples will yield the 
interpretation output.

## Usage

Three parameters required to run the CLI are as follows:

1. *directory* - directory containing all the existing test files to be analysed;
1. *testFileExtension* - configurable extension type of the file;
1. *reportType* - report type to be outputed by the Testalyser, see [Report types](#report-types).  

### Report types

The Testalyser has 5 analysis types, described below:
 
1. ALL_TESTS - outputs a report of all the tests parsed, nothing special;
1. FLAKY_TESTS - outputs a report of flaky tests, tests that have registered a failure in the past and their current status;
1. PASSED_TESTS - outputs a report of passed tests, duration included;
1. FAILED_TESTS - outputs a report of failed tests, duration included;
1. ALL_TESTS_DURATION_SORTED - outputs a report of all tests, sorted in descending order based on duration;

### Examples

```bash
λ java -jar %path_to%testalyser-1.0-SNAPSHOT.jar tm-challenge/ .txt ALL_TESTS  

Testalyser analysis type: [ALL_TESTS].

Analysis completed for files: [graph_test.txt, lock_test_1.txt, lock_test_2.txt, rewrite_test.txt].

--------------------------------------------------
| TEST                      | STATUS | DURATION  |
--------------------------------------------------
| TestAddTarget             | PASS   | 0.00s     |
| TestAddPackage            | PASS   | 0.00s     |
| TestTarget                | PASS   | 0.00s     |
| TestRevDeps               | PASS   | 0.00s     |
| TestAllDepsResolved       | PASS   | 0.00s     |
| TestDependentTargets      | PASS   | 0.00s     |
| TestSubrepo               | PASS   | 0.00s     |
| TestAllDepsBuilt          | SKIP   | 0.00s     |
| TestReadLastOperation     | PASS   | 0.00s     |
| TestAcquireRepoLock       | FAIL   | 1.02s     |
| TestAcquireRepoLock       | PASS   | 0.00s     |
| TestReadLastOperation     | PASS   | 0.00s     |
| TestRewriteFile           | PASS   | 0.01s     |
--------------------------------------------------
```

```bash
λ java -jar java -jar testalyser-1.0-SNAPSHOT.jar tm-challenge/ .txt FLAKY_TESTS

Testalyser analysis type: [FLAKY_TESTS].

Analysis completed for files: [graph_test.txt, lock_test_1.txt, lock_test_2.txt, rewrite_test.txt].

---------------------------------------------
| TEST                      | LATEST_STATUS |
---------------------------------------------
| TestAcquireRepoLock       | PASS          |
---------------------------------------------
```

You get the idea :)

> Note: Make sure you've given the "executor" of choice the rights to read the test folder. Otherwise - empty report.

## TODO's / "Smells"

* Introduce a templating engine, something lightweight like [mustache](https://mustache.github.io/), will ease of a lot
of the pain we are facing when generating the report;
* Allow chaining of analysis types, e.g:
`java -jar java -jar testalyser-1.0-SNAPSHOT.jar tm-challenge/ .txt FLAKY_TESTS DURATION_ORDER_DESCENDING`, instead of
having explicit combinations, which do not scale very well.
* Allow the loading from multiple directories as well as multiple test types, all through parametrisation.
* Introduce a proper CLI library, allowing us to build proper a proper CLI utility, help and all. See [picocli](https://picocli.info/).
* A clear distinction between a test and a test run, currently it is hacked together to represent both through unsavory
overriding of the equals() method, medium-sized no-no;
* Addition of assertion library that support Harmcrest-like matchers;
* Setup CI pipeline, Travis or Circle should do just fine;
* Capture the output of tests, where present as well as print skip reason for SKIP status tests;

## Languages, frameworks and libraries

* *Java 11*, programming language of choice;
* *Gradle 6.1.1*, dependency management and build tool;
* *JUnit 5*, used for purposes of, "unit" testing; 
