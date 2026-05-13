# Contribution Flow

This note captures a small, reviewable contribution flow for this repository.

## Before Opening a Pull Request

- Keep the change focused on one issue or requirement.
- Avoid unrelated formatting, dependency, or generated-file changes.
- Check whether the same issue already has an open pull request.
- Record the affected module, such as `ruoyi-ui`, `ruoyi-admin`, or shared backend modules.

## Pull Request Checklist

- Link the issue with `fixes #<number>` when the change is intended to close it.
- Summarize what changed and why it is safe.
- Include verification steps, even when the change is documentation-only.
- Confirm that no secrets, private keys, tokens, or local environment files are included.

## Review Notes

Small documentation-only updates can usually be verified with:

```bash
git diff --check
```

Code changes should include the narrowest practical build, lint, or test command for the touched module.
