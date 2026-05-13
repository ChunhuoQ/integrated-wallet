# Integrated Wallet Project Notes

This repository is based on the RuoYi Spring Boot and Vue stack. The notes below
summarize the parts that are most useful when reviewing or extending the project.

## Repository Layout

- `ruoyi-admin`: backend application entry point and runtime configuration.
- `ruoyi-common`: shared Java utilities, constants, annotations, and helpers.
- `ruoyi-framework`: security, web, datasource, and framework integration code.
- `ruoyi-system`: core system modules such as users, roles, menus, and logs.
- `ruoyi-quartz`: scheduled task support.
- `ruoyi-generator`: code generation support for CRUD modules.
- `ruoyi-ui`: Vue and Element UI frontend application.
- `sql`: database initialization and upgrade scripts.

## Local Run Checklist

1. Import the SQL scripts from `sql` into the configured database.
2. Review datasource, Redis, and server settings in `ruoyi-admin/src/main/resources`.
3. Start the backend from the `ruoyi-admin` module.
4. Install frontend dependencies in `ruoyi-ui` and run the local development server.
5. Sign in with a local administrator account and verify menu, role, and dashboard pages.

## Change Review Checklist

- Keep backend changes in the matching module instead of adding unrelated code to
  the admin entry point.
- Keep frontend API calls, views, and route definitions aligned when adding a new
  page.
- Update SQL migrations when a feature needs new tables, columns, or seed data.
- Run backend and frontend checks before opening a pull request.
- Include clear reproduction and verification notes for bug fixes.

## Useful Extension Points

- Add new business entities through `ruoyi-generator` when a standard CRUD flow is
  enough.
- Place shared Java helpers in `ruoyi-common` only when more than one module uses
  them.
- Add reusable frontend components under `ruoyi-ui/src/components`.
- Keep environment-specific frontend URLs in the existing `.env.*` files.
