# Campus Lost & Found - Java Web Application (Servlets + JSP + MySQL)

Runs in the browser on Apache Tomcat. Same features and school design as the PHP website,
but written in Java with the OOP structure from your lessons.

## 1. Install (once)
1. **JDK 17 or newer**
2. **Apache NetBeans 17 or newer** (https://netbeans.apache.org)
3. **Apache Tomcat 10.1** - download the "zip" (Core) from https://tomcat.apache.org and extract it, e.g. `C:\apache-tomcat-10.1.x`
4. **XAMPP** (only MySQL is needed)

## 2. Database
1. Start MySQL in XAMPP.
2. phpMyAdmin > Import > `database/schema.sql` > Go.
3. If your MySQL root has a password, edit `src/main/resources/db.properties`.

## 3. Connect Tomcat to NetBeans (once)
Services tab > right-click **Servers** > **Add Server...** > **Apache Tomcat or TomEE** > Next >
Server Location = your Tomcat folder > enter a username/password (e.g. admin / admin) and keep "Create user if it does not exist" ticked > Finish.

## 4. Run the project
1. File > **Open Project** > choose the `CampusLostFoundWeb` folder. Wait for Maven to download libraries.
2. Right-click the project > **Properties** > **Run** > Server = **Apache Tomcat**, Relative URL = `/` > OK.
3. Right-click the project > **Clean and Build**.
4. Right-click the project > **Run** (or press F6). The browser opens `http://localhost:8080/CampusLostFound/`.

## 5. Logins
- Admin: `admin` / `admin123` (created automatically)
- Student: a number like `25-22-081` + full name, no password

## 6. How the pieces map to the lessons
| Layer | Classes | Lessons |
|-------|---------|---------|
| Model | `User`, `Student`, `Admin`, `Item`, `LostItem`, `FoundItem`, `Claim` | classes, constructors, encapsulation, inheritance, abstraction, polymorphism, interfaces |
| Exceptions | `exception` package | custom checked/unchecked exceptions, try/catch |
| DAO (JDBC) | `dao` package, `Repository<T,ID>` | generics, JDBC, database |
| Services | `ItemService`, `ClaimService`, `AuthService` ... | methods, overloading, collections (ArrayList, HashSet, HashMap) |
| Controllers | `web` package: `BaseServlet` (abstract) + one servlet per page, `AuthFilter`, `AppInitListener` | inheritance, abstract classes, polymorphism |
| Views | `WEB-INF/views/*.jsp`, `css/style.css` | HTML + CSS |
| Files | `AppLogger`, CSV export (`/admin/transactions?export=1`) | file handling |

## 7. Troubleshooting
- **Tomcat not accepted by NetBeans**: update NetBeans, or use Tomcat 10.0.x.
- **Port 8080 busy**: change the port in Tomcat's `conf/server.xml` or stop the other program.
- **HTTP 500 / "Cannot connect to MySQL"**: start MySQL, import `schema.sql`, check `db.properties`.
- **404**: use the URL NetBeans opens; the app path should be `/CampusLostFound/`.
- **Red errors after opening**: let Maven finish, then right-click project > Clean and Build.
