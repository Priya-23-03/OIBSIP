Unit Converter Android Application

Project Overview

The Unit Converter is an Android application developed using Java in
Android Studio. It enables users to convert values between different
units of measurement through a simple and user-friendly interface.

The application supports seven conversion categories and provides
features such as unit swapping, conversion history, and clearing saved
history.

Objectives

-   Develop a user-friendly Android application for unit conversion.
-   Perform conversions across multiple measurement categories.
-   Implement unit swapping for convenient conversions.
-   Store and manage conversion history locally.
-   Build, install, and test a signed release APK.

Features

-   Seven Conversion Categories: Length, Weight, Temperature, Area,
    Volume, Speed, and Time.
-   Unit Conversion: Convert values between supported units.
-   Swap Units: Switch the source and target units.
-   Conversion History: View previous conversions.
-   Clear History: Remove saved conversion records.
-   Splash Screen: Display a launch screen when opening the app.
-   Input Validation: Handle invalid inputs and temperature values
    below absolute zero.
-   Simple UI: Blue-and-white interface designed for ease of use.

Technologies Used

-   Programming Language: Java
-   IDE: Android Studio
-   UI Design: XML
-   Local Storage: Android SharedPreferences
-   Build Output: Signed Android APK

Supported Conversion Categories

  -----------------------------------------------------------------------
  Category                            Supported Units
  ----------------------------------- -----------------------------------
  Length                              Meter, Kilometer, Centimeter, Inch

  Weight                              Kilogram, Gram, Pound

  Temperature                         Celsius, Fahrenheit, Kelvin

  Area                                Square Meter, Square Kilometer,
                                      Square Centimeter, Hectare, Acre

  Volume                              Liter, Milliliter, Cubic Meter, US
                                      Gallon

  Speed                               Meter/Second, Kilometer/Hour,
                                      Mile/Hour

  Time                                Second, Minute, Hour, Day
  -----------------------------------------------------------------------

How the Application Works

1.  Open the Unit Converter application.
2.  Select a conversion category.
3.  Enter the value to be converted.
4.  Select the source unit and target unit.
5.  View the converted result.
6.  Use the swap option to reverse the selected units.
7.  View previous conversions in the history section.
8.  Clear the history when required.

Data Storage

The application uses Android SharedPreferences to store conversion
history locally on the device. The history feature does not require a
remote database.

Installation

Install the APK

1.  Obtain the signed release APK.
2.  Transfer the APK to an Android device.
3.  Open the APK using the device's file manager.
4.  Allow installation from the relevant source if prompted.
5.  Tap **Install** and open the application.

Run the Source Code

1.  Clone or download this repository.
2.  Open Android Studio.
3.  Open the Unit Converter project folder.
4.  Allow Gradle synchronization to complete.
5.  Connect an Android device or start an emulator.
6.  Run the application.














Screenshots
Recommended screenshots:


Fig 1. Homescreen                                          Fig 2. Length conversion


















              Fig 4. Recent conversions


Fig 3. Temperature conversion















Fig 5. Clear history

Testing

The application was installed and tested on an Android device. Testing
included:

-   Application launch and navigation
-   Supported unit conversions
-   Unit swapping
-   Conversion history
-   Clearing conversion history
-   Access to all seven conversion categories

Learning Outcomes

Through this project, I gained practical experience in:

-   Android application development using Java
-   Designing user interfaces with XML
-   Implementing conversion algorithms
-   Validating user inputs
-   Storing data using SharedPreferences
-   Generating a signed release APK
-   Installing and testing an application on a physical Android device

Future Enhancements

-   Add more measurement units.
-   Introduce dark mode.
-   Improve accessibility and user experience.
-   Add additional customization options.








Author

Priyadharshini

Android Application Development Project

Internship Submission

Repository: [OIBSIP](https://github.com/Priya-23-03/OIBSIP)

This README documents the Unit Converter Android application developed
as part of practical software development learning.