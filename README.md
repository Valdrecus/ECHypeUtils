# 🌐 ECHypeUtils Expansion
Una expansión ligera y útil para PlaceholderAPI que detecta jugadores sospechosos por uso de VPN o por tener cuentas recién creadas.

---

## 📦 Placeholder:
### `%echypeutils_sospechoso%` `%echypeutils_sospechoso_[USUARIO]%`

Evalúa si un jugador es considerado **sospechoso** mediante dos criterios clave:

### 🔎 Criterios de evaluación:

- 🛡️ **VPN / Proxy / Hosting / Mobile**  
  Se realiza una consulta a `ip-api.com` para detectar si el jugador usa:
  - VPN
  - Proxy
  - Hosting
  - Red móvil

- ⏰ **Antigüedad de la cuenta**  
  Verifica si la cuenta fue registrada hace **menos de 1 día** y tiene más de 30 minutos jugados.

---

### ✅ Posibles resultados:

| Resultado         | Significado                                                                 |
|-------------------|------------------------------------------------------------------------------|
| `Limpio`          | No usa VPN y su cuenta fue creada hace menos de 1 día                       |
| `VPN`             | El jugador usa VPN, proxy, red móvil o servidor de hosting                  |
| `VPN (error)`     | La API respondió con error                                                   |
| `VPN (fallo)`     | No se pudo completar la consulta a la API (timeout, fallo de red, etc.)     |
| `cuenta vieja`    | La cuenta fue registrada hace más de 1 día                                  |

> ⚠️ **Solo se considera "Limpio" si cumple ambos criterios: sin VPN y cuenta recién creada.**

---

## ⚙️ Requisitos

- [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
- Java 17+
- Servidor Paper/Spigot compatible

---

## 🧪 Ejemplo de uso

Puedes usar este placeholder en hologramas, chat, menús, Scoreboards, etc.

```yml
&7Estado de confianza: &f%echypeutils_sospechoso% %echypeutils_sospechoso_[Valdrecus]%
