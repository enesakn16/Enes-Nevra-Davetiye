# Security Policy

## Supported versions

This repository is an actively improved portfolio project. Security fixes are applied to the latest version on the `main` branch only.

| Version | Supported |
| --- | --- |
| Latest `main` | Yes |
| Older commits and artifacts | No |

## Reporting a vulnerability

Do not open a public issue containing passwords, tokens, personal data, exploit details, or other sensitive information.

Report security concerns privately through the repository owner's GitHub profile contact options. Include:

- A clear description of the issue
- The affected file, screen, or workflow
- Reproduction steps
- Expected and actual behavior
- The potential impact
- A suggested fix, when available

Please allow reasonable time for investigation before publishing details.

## Current known limitations

- The legacy local profile flow stores credentials in plain `SharedPreferences`. Do not enter a real password. This flow must be replaced rather than presented as production authentication.
- The legacy registration implementation has written sensitive form values to Android debug logs. Logging secrets or personal data is prohibited and scheduled for removal.
- Notification handling requires a proper Android 13+ `POST_NOTIFICATIONS` runtime-permission flow and must not treat an in-app explanation dialog as system permission approval.
- Debug APK artifacts are development builds and are not signed production releases.

## Security expectations for contributions

- Never commit secrets, signing keys, service-account files, private API keys, or production credentials.
- Use least-privilege GitHub Actions permissions.
- Keep Gradle and GitHub Actions dependencies reviewed and current.
- Add tests for security-sensitive parsing, authentication, storage, and permission behavior.
- Avoid logging credentials, tokens, email addresses, health data, or other sensitive user information.
