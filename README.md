# auth-api

FIXME: description

## Installation

Download from https://example.com/FIXME.

## Usage

FIXME: explanation

    $ java -jar auth-api-0.1.0-standalone.jar [args]

## Options

FIXME: listing of options this app accepts.

## Examples

...

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2026 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
https://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.

<!-- ************************************************************ -->

---

# 🟢 1️⃣ Cadastro

## 🔹 Sucesso — 201 Created

```json
{
  "success": true,
  "data": {
    "id": "uuid-123",
    "email": "usuario@email.com"
  }
}
```

Status: **201**

---

## 🔹 Email já existe — 409 Conflict

```json
{
  "success": false,
  "error": {
    "code": "email_already_exists",
    "message": "O email informado já está em uso."
  }
}
```

Status: **409**

---

## 🔹 Senhas diferentes — 400 Bad Request

```json
{
  "success": false,
  "error": {
    "code": "password_mismatch",
    "message": "As senhas não conferem."
  }
}
```

Status: **400**

---

# 🟢 2️⃣ Login

## 🔹 Sucesso — 200 OK

```json
{
  "success": true,
  "data": {
    "access_token": "jwt-token",
    "refresh_token": "refresh-token",
    "expires_in": 600
  }
}
```

Status: **200**

`expires_in` em segundos (boa prática).

---

## 🔹 Credenciais inválidas — 401 Unauthorized

```json
{
  "success": false,
  "error": {
    "code": "invalid_credentials",
    "message": "Email ou senha inválidos."
  }
}
```

Status: **401**

---

# 🟢 3️⃣ Refresh

## 🔹 Sucesso — 200 OK

```json
{
  "success": true,
  "data": {
    "access_token": "novo-jwt-token",
    "expires_in": 600
  }
}
```

Status: **200**

---

## 🔹 Refresh inválido — 401 Unauthorized

```json
{
  "success": false,
  "error": {
    "code": "invalid_refresh_token",
    "message": "Sessão inválida. Faça login novamente."
  }
}
```

Status: **401**

---

# 🟢 4️⃣ Logout

## 🔹 Sucesso — 200 OK

```json
{
  "success": true,
  "data": {
    "message": "Logout realizado com sucesso."
  }
}
```

Status: **200**

---

# 🟢 5️⃣ Usuário (rota protegida)

## 🔹 Sucesso — 200 OK

```json
{
  "success": true,
  "data": {
    "id": "uuid-123",
    "email": "usuario@email.com"
  }
}
```

---

## 🔹 Token expirado — 401 Unauthorized

```json
{
  "success": false,
  "error": {
    "code": "token_expired",
    "message": "O token expirou."
  }
}
```

# 🎯 Padrão final resumido

| Endpoint | Sucesso | Erro principal |
| -------- | ------- | -------------- |
| Cadastro | 201     | 400 / 409      |
| Login    | 200     | 401            |
| Refresh  | 200     | 401            |
| Logout   | 200     | —              |
| Usuário  | 200     | 401            |

---
