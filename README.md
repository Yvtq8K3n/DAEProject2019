# DAEProject2019

This project aimed to implement an Enterprise Application following the [3-Layer Module Architecture](https://martinfowler.com/bliki/PresentationDomainDataLayering.html).

```diff
3-Layer Module [whit JaveEE 7]
 |------------------------------------------|
 | Presentation Logic Layer[PLL]            |
 |       ○ ------------------->  ≣  ≣      | 
 |  AdminManager              xhtml pages   |
 |       |       whit Template View Pattern |   
 |       v                   Bootfaces[JSF] |
 |------------------------------------------| Use Rest to access Service Layer
 
-|------------------------------------------|  Service Layer [whit Jersey]
 | Business Logic Layer[BLL]                |
 |     ⦼                      ⦼           |
 | ManagedBean            ManagedBean       |                                     ¸.···.¸
 |     v                       v            |       |≣≣≣≣|                      |·.¸¸.·|
+|----------------------Entity Manager[EM]--|------>|≣≣≣≣| ---- Connect BD----->|      |
 | Data Access Layer[DAL]        ^          |       |≣≣≣≣|                      |¸.··.¸|
 |                           Entities       |                                     `·...·´
 |                     JPA whit EclipseLink |    persistence.xml                DAE[JAVA BD]
 |------------------------------------------|                                                       
```


The developed solution applies the following frameworks/tools:
* Java EE 7
* JPA with EclipseLink for entities
* Bootfaces(xhtml.pages) - Java Servelt Faces[JSF]
* Lombok

This project tries to fulfill the User Stories listed in the following file:
[UserStories.txt](https://github.com/Yvtq8K3n/DAEProject2019/blob/master/UserStories)
