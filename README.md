### README FOR TESTS
Additional deliverables are under `additional_deliverables` file!
Use Android Studio to run our tests!
Please find our Jacoco results for the GUI tests in the folder titled `Jacoco`; then, under `reports` > `coverage` > `androidTest` > `debug` > `connected` should be an `index.html` file, that you can right-click on then open with your preferred browser.
To generate coverage results for the unit tests, please read below.

In order to run our tests, especially our GUI tests, you need an emulator set up that runs Android API 27. To do so follow the instructions provided by Android:
[(https://developer.android.com/studio/run/managing-avds)]

Once you get the emulator up and running, you can run our unit tests and application tests in tandem by navigating to the `build.gradle` file in the app folder (NOT the one in the project folder), then pressing the play button on line 17 beside `task jacocoTestReport`.
Alternatively, if you would rather run our tests individually, navigate to either the `ApplicationTest` file to run the GUI tests, or the `test` folder for the units tests, that are ran separated by Activity test. After navigating to the desired test suite you would like to run, press the play button next to the line number of the class name, which will run all of the tests in that file.

If you would like to generate the Jacoco report for yourself to see the coverage report, you must run the tests in tandem from the app `build.gradle` file, with the instructions from above. If you would like to generate the coverage report for the unit tests, right click on the `test` or `unitTest` file then select `run with coverage`.

Additionally, if you find that you have trouble running these deliverables (which shouldn't be the case), or would like to view our github, please pull from the below link!
[(https://github.com/lidiaberhe/privacy-friendly-2048-jhu-st.git)]


### ORIGINAL README
[![Twitter](https://img.shields.io/badge/twitter-@SECUSOResearch-%231DA1F2.svg?&style=flat-square&logo=twitter&logoColor=1DA1F2)][Twitter]
[![Mastodon](https://img.shields.io/badge/mastodon-@SECUSO__Research@baw%C3%BC.social-%233088D4.svg?&style=flat-square&logo=mastodon&logoColor=3088D4)][Mastodon]

[Mastodon]: https://xn--baw-joa.social/@SECUSO_Research
[Twitter]: https://twitter.com/SECUSOResearch
# Privacy Friendly 2048

The Privacy Friendly 2048 app is an addictive puzzle game. The game is considered to be won if you reach the number 2048 by pushing the same numbers together. It belongs to the group of Privacy Friendly Apps supported by the research group SECUSO.

This app is the optimal gadget to protect the user's privacy and at the same time playing 2048. The app can provide its full functionality without the need of any permission. Furthermore it does not use any tracking mechanisms or advertisement as other apps in the stores. 

[<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png"
     alt="Get it on Google Play"
     height="80">](https://play.google.com/store/apps/details?id=org.secuso.privacyfriendly2048)
[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="80">](https://f-droid.org/packages/org.secuso.privacyfriendly2048/)

## Motivation

As many apps require a lot of the users information’s and also include advertisement, we design this privacy friendly alternative to provide only the functionalities without the needless inquerement of data. Supported by the SECUSO research group, the app optimized the user's privacy during the usage.

## Download and more Information

Further development requires Android Studio, we recommend to use at least version 3.1.1
For more information about the app and how it works, please take a look at the Wiki section.
 
### API Reference

Mininum SDK: 21
Target SDK: 27 

## License

Privacy Friendly 2048 is licensed under the GPLv3.
Copyright (C) 2018  Saskia Jacob and Julian Wadephul

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.

The icons used in the nagivation drawer are licensed under the [CC BY 2.5](http://creativecommons.org/licenses/by/2.5/). In addition to them the app uses icons from [Google Design Material Icons](https://design.google.com/icons/index.html) licensed under Apache License Version 2.0. All other images (the logo of Privacy Friendly 2048, the SECUSO logo and the app logo) copyright [Karlsruhe Institute fo Technology (KIT)](www.kit.edu) (2018).

## Contributors

App-Icon: Tatjana Albrandt

Developer: Saskia Jacob and Julian Wadephul





