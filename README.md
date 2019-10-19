#### [SMS Forwarding](https://github.com/warren-bank/Android-SMS-Automatic-Forwarding)

Android app that listens for incoming SMS text messages and conditionally forwards them to other numbers.

#### Screenshots:

![SMS-Forwarding](./screenshots/1-prefs-no-entries-in-whitelist.png)
![SMS-Forwarding](./screenshots/2-prefs-add-new-entry-dialog.png)
![SMS-Forwarding](./screenshots/3-prefs-one-entry-in-whitelist.png)

#### Preferences:

* `Enable Service` checkbox:
  * used to enable/disable this service
* `ADD` ActionBar menu item:
  * adds new recipient to whitelist
* whitelist entries are defined as follows:
  * `Forwarding recipient`:
    * a valid phone number (without any punctuation)
  * `Sender must end with`:
    * this value specifies a phone number (without any punctuation)
      * a match occurs when the _sender_ of an incoming SMS message ends with this exact value
      * a special match-all glob pattern `*` is supported
    * this value acts as a filter
      * `Forwarding recipient` will only receive copies of incoming SMS messages that match this value
* whitelist entries can be modified
  * clicking on an existing recipient opens a dialog with options to:
    * edit field values, and save changes
    * delete

#### Caveats:

* _Google Voice_:
  * if:
    * the [_Google Voice_ app](https://play.google.com/store/apps/details?id=com.google.android.apps.googlevoice) is installed on the phone
    * the _Google Voice_ account that the app is logged into:
      * registers the _real_ mobile phone number as a forwarding number
      * configures incoming SMS text messages to be forwarded as well
  * when:
    * a SMS text message is sent to the _Google Voice_ _virtual_ mobile phone number
  * then:
    * the phone receives an incoming SMS text message
  * but:
    * the sender of the SMS message will always be a number belonging to the _Google Voice_ backend infrastructure

#### Notes:

* minimum supported version of Android:
  * Android 3.0 (API level 11)

#### Legal:

* copyright: [Warren Bank](https://github.com/warren-bank)
* license: [GPL-2.0](https://www.gnu.org/licenses/old-licenses/gpl-2.0.txt)
