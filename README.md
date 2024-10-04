# minibus-app
MVP + Java + RxJava2 + Dagger 2 + ButterKnife + Retrofit 2

A simple demo application for displaying bus schedules among 6 cities, which also includes a calendar of operating days, timeline filtering, user session, and trips booking management. Supports light and dark themes.

Designed for demo purposes only. Depends on [minibus-service](https://github.com/n3gbx/minibus-service) repository

[app-debug.apk](./demo/app-debug.apk), points to locally running service at 10.0.2.2:3000 (using emulator)

[app-stage.apk](./demo/app-stage.apk), points to cloud based service

## Recordings demonstrating core functionality
|Authentication|Filtering|Sorting|Booking|
|------|------|------|------|
|<video src="https://github.com/user-attachments/assets/5f7791ce-34a4-4aee-b389-af7dc0483f82" height="480">|<video src="https://github.com/user-attachments/assets/ec79cbde-56b9-478d-91d7-37974fdae8bc" height="480">|<video src="https://github.com/user-attachments/assets/699f05c2-6329-4ba5-b2a6-fd26b7d431b3" height="480">|<video src="https://github.com/user-attachments/assets/3e518a0a-8699-4592-a7a0-3169e5269347" height="480">|

## Architecture diagram
<img src="./demo/architecture_diagram.svg" height="640" alt="Architecture diagram">

