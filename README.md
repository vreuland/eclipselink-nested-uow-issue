This project demonstrates an issue encountered when trying to update EclipseLink from version 2.7.6 to 2.7.7 (or 2.7.10). The issue might be related to the following bug fixed
in 2.7.7:
[New Object created and deleted in nested UnitOfWork is commited to DB #624](https://github.com/eclipse-ee4j/eclipselink/issues/624)

### Test case scenario

Given two (versioned) entities linked using a _OneToMany_ relation (with _orphanRemoval_):

```
Order --[OneToMany]--> LineItem
```

When we perform the following scenario:

1. Create a nested UOW
2. Retrieve (query) an Order with one LineItem
3. Create and register a second LineItem and attach it to the Order
4. Delete all LineItem and then the Order
5. Commit the nested UOW and then the parent UOW

Then,

- when using EclipseLink 2.7.6, the scenario is successful (no error)
- when using EclipseLink 2.7.7, the scenario is reporting an `OptimisticLockException`:
  ```
    Caused by: javax.persistence.OptimisticLockException: Exception [EclipseLink-5003] (Eclipse Persistence Services - 2.7.7.v20200504-69f2c2b80d): org.eclipse.persistence.exceptions.OptimisticLockException
    Exception Description: The object [model.LineItem@1623134f] cannot be deleted because it has changed or been deleted since it was last read. 
    Class> model.LineItem Primary Key> 2
  ```

### How to run the test

Update the `pom.xml` to switch between EclipseLink version 2.7.6 <-> 2.7.7, then execute:

```
mvn clean test
```
