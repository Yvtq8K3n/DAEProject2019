# DAEProject2019

This is a Enterprise Application made on Netbeans that implements the 3-layer module architecture.

```diff
3-Layer Module [whit JaveEE 7]
 |------------------------------------------|
 | Presentation Logic Layer[PLL]            |
 |       ○ ------------------->  ≣  ≣      | 
 |  AdminManager              xhtml pages   |
 |              whit Transform View Pattern |   
 |                           Bootfaces[JSF] |
 |------------------------------------------|
 
-|------------------------------------------|  Service Layer [whit Jersey]
 | Business Logic Layer[BLL]                |
 |     ⦼                      ⦼           |
 | ManagedBean            ManagedBean       |                                                    ¸.···.¸
 |     v                       v            |                 |≣≣≣≣|                           |·.¸¸.·|
+|----------------------Entity Manager[EM]--|---------------->|≣≣≣≣| --------Connect BD------->|      |
 | Data Access Layer[DAL]        ^          |                 |≣≣≣≣|                           |¸.··.¸|
 |                           Entities       |                                                    `·...·´
 |                     JPA whit EclipseLink |              persistence.xml                    DAE[JAVA BD]
 |------------------------------------------|                                                       


```


It combines the following frameworks:
* Java EE 7
* JPA whit EclipseLink for entities
* Bootfaces(xhtml.pages) - Java Servelt Faces[JSF]

This project trys to fulfil the User Stories listed on the following file:
[UserStories.txt](https://github.com/Yvtq8K3n/DAEProject2019/blob/master/UserStories)
