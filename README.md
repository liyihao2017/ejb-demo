---
typora-root-url: src\main\resources
typora-copy-images-to: src\main\resources
---

ejb-demo(No code  temporarily)
==============

A simple project for company internal show how to use EJB in Java EE7.

Here is a brief instruction I have learned from official website.



Technology:  

​	font end: Vaadin

​        back end: Java EE7





# 1.  Types of Enterprise Java Bean

- Session Bean
- Message-Driven Bean

## 1.1  Session Bean

- Stateful
- Stateless
- Singleton

### 1.1.1  When to use Session Beans

Here is from official website:

Stateful session beans are appropriate if any of the following conditions are true.

- The bean's state represents the interaction between the bean and a specific client.
- The bean needs to hold information about the client across method invocations.
- The bean mediates between the client and the other components of the application, presenting a simplified view to the client.
- Behind the scenes, the bean manages the work flow of several enterprise beans.

To improve performance, you might choose a stateless session bean if it has any of these traits.

- The bean's state has no data for a specific client.
- In a single method invocation, the bean performs a generic task for all clients. For example, you might use a stateless session bean to send an email that confirms an online order.
- The bean implements a web service.

Singleton session beans are appropriate in the following circumstances.

- State needs to be shared across the application.
- A single enterprise bean needs to be accessed by multiple threads concurrently.
- The application needs an enterprise bean to perform tasks upon application startup and shutdown.
- The bean implements a web service.

## 1.2  Message-Driven Bean

process messages asynchronously.

### 1.2.1  Different from Session Bean

clients do not access message-driven beans through interfaces.Unlike a session bean, a message-driven bean has only a bean class.

### 1.2.2  When to Use Message-Driven Beans

To receive messages asynchronously, use a message-driven bean.

# 2.  Accessing Enterprise Beans

two ways:

1. dependency injection
2. JNDI lookup

syntax:

- The `java:global` JNDI namespace is the portable way of finding remote enterprise beans using JNDI lookups. Application name and module name default to the name of the application and module minus the file extension. Application names are required only if the application is packaged within an **EAR**. The interface name is required only if the enterprise bean implements more than one business interface.

```java
java:global[/application name]/module name /enterprise bean name[/interface name ]
```



-    The `java:module` namespace is used to look up local enterprise beans within the same module.The interface name is required only if the enterprise bean implements more than one business interface.

```java
java:module/enterprise bean name/[interface name]
```



- The `java:app` namespace is used to look up local enterprise beans packaged within the same application. That is, the enterprise bean is packaged within an EAR file containing multiple Java EE modules.

```java
java:app[/module name]/enterprise bean name [/interface name]
```

## 2.1  Deciding on Remote or Local Access

Care about 4 factors:

1. Tight or loose coupling of related beans
2. Type of client
3. Component distribution
4. Performance

**If you aren't sure which type of access an enterprise bean should have, choose remote access. This decision gives you more flexibility. In the future, you can distribute your components to accommodate the growing demands on your application.**



## 2.2  Local Clients

A local client has these characteristics.

- It must run in the same application as the enterprise bean it accesses.
- It can be a web component or another enterprise bean.
- To the local client, the location of the enterprise bean it accesses is not transparent.

The no-interface view of an enterprise bean is a local view. The public methods of the enterprise bean implementation class are exposed to local clients that access the no-interface view of the enterprise bean. Enterprise beans that use the no-interface view do not implement a business interface.

The **local business interface** defines the bean's business and lifecycle methods. If the bean's business interface is not decorated with `@Local` or `@Remote`, and if the bean class does not specify the interface using `@Local` or `@Remote`, the business interface is by default a local interface.

To build an enterprise bean that allows only local access, you may, but are not required to, do one of the following.

- Create an enterprise bean implementation class that does not implement a business interface, indicating that the bean exposes a no-interface view to clients. For example:

  ```
  @Session
  public class MyBean { ... }
  ```

- Annotate the business interface of the enterprise bean as a `@Local` interface. For example:

  ```
  @Local
  public interface InterfaceName { ... }
  ```

- Specify the interface by decorating the bean class with `@Local` and specify the interface name. For example:

  ```
  @Local(InterfaceName.class)
  public class BeanName implements InterfaceName  { ... }
  ```



### 2.2.1  Accessing Local Enterprise Beans Using the No-Interface View

Client access to an enterprise bean that exposes a local, no-interface view is accomplished through either dependency injection or JNDI lookup.

- To obtain a reference to the no-interface view of an enterprise bean through dependency injection, use the `javax.ejb.EJB` annotation and specify the enterprise bean's implementation class:

  ```
  @EJB
  ExampleBean exampleBean;
  ```

- To obtain a reference to the no-interface view of an enterprise bean through JNDI lookup, use the `javax.naming.InitialContext` interface's `lookup` method:

  ```
  ExampleBean exampleBean = (ExampleBean)
          InitialContext.lookup("java:module/ExampleBean");
  ```

Clients *do not* use the `new` operator to obtain a new instance of an enterprise bean that uses a no-interface view.



### 2.2.2  Accessing Local Enterprise Beans That Implement Business Interfaces

Client access to enterprise beans that implement local business interfaces is accomplished through either dependency injection or JNDI lookup.

- To obtain a reference to the local business interface of an enterprise bean through dependency injection, use the `javax.ejb.EJB` annotation and specify the enterprise bean's local business interface name:

  ```
  @EJB
  Example example;
  ```

- To obtain a reference to a local business interface of an enterprise bean through JNDI lookup, use the `javax.naming.InitialContext` interface's `lookup`method:

  ```
  ExampleLocal example = (ExampleLocal)
           InitialContext.lookup("java:module/ExampleLocal");
  ```



## 2.3  Remote Clients

A remote client of an enterprise bean has the following traits.

- It can run on a different machine and a different JVM from the enterprise bean it accesses. (It is not required to run on a different JVM.)
- It can be a web component, an application client, or another enterprise bean.
- To a remote client, the location of the enterprise bean is transparent.
- The enterprise bean must implement a business interface. That is, remote clients *may not* access an enterprise bean through a no-interface view.

To create an enterprise bean that allows remote access, you must either

- Decorate the business interface of the enterprise bean with the `@Remote` annotation:

  ```
  @Remote
  public interface InterfaceName { ... }
  ```

- Or decorate the bean class with `@Remote`, specifying the business interface or interfaces:

  ```
  @Remote(InterfaceName.class)
  public class BeanName implements InterfaceName { ... }
  ```

The **remote interface** defines the business and lifecycle methods that are specific to the bean. For example, the remote interface of a bean named `BankAccountBean`might have business methods named `deposit` and `credit`. [Figure 32-1](https://docs.oracle.com/javaee/7/tutorial/ejb-intro004.htm#GIPNO) shows how the interface controls the client's view of an enterprise bean.



![jeett_dt_020](/jeett_dt_020.png)

​					Figure 32-1 Interfaces for an Enterprise Bean with Remote Access



Description of "Figure 32-1 Interfaces for an Enterprise Bean with Remote Access"

Client access to an enterprise bean that implements a remote business interface is accomplished through either dependency injection or JNDI lookup.

- To obtain a reference to the remote business interface of an enterprise bean through dependency injection, use the `javax.ejb.EJB` annotation and specify the enterprise bean's remote business interface name:

  ```
  @EJB
  Example example;
  ```

- To obtain a reference to a remote business interface of an enterprise bean through JNDI lookup, use the `javax.naming.InitialContext` interface's `lookup`method:

  ```
  ExampleRemote example = (ExampleRemote)
          InitialContext.lookup("java:global/myApp/ExampleRemote");
  ```





## 2.4  Web Service Clients

A web service client can access a Java EE application in two ways. First, the client can access a web service created with JAX-WS. (For more information on JAX-WS, see [Chapter 28, "Building Web Services with JAX-WS"](https://docs.oracle.com/javaee/7/tutorial/jaxws.htm#BNAYL).) Second, a web service client can invoke the business methods of a stateless session bean. Message beans cannot be accessed by web service clients.

Provided that it uses the correct protocols (SOAP, HTTP, WSDL), any web service client can access a stateless session bean, whether or not the client is written in the Java programming language. The client doesn't even "know" what technology implements the service: stateless session bean, JAX-WS, or some other technology. In addition, enterprise beans and web components can be clients of web services. This flexibility enables you to integrate Java EE applications with web services.

A web service client accesses a stateless session bean through the bean's web service endpoint implementation class. By default, all public methods in the bean class are accessible to web service clients. The `@WebMethod` annotation may be used to customize the behavior of web service methods. If the `@WebMethod` annotation is used to decorate the bean class's methods, only those methods decorated with `@WebMethod` are exposed to web service clients.

For a code sample, see [A Web Service Example: helloservice](https://docs.oracle.com/javaee/7/tutorial/ejb-basicexamples003.htm#BNBOR).





## 2.4.1  Method Parameters and Access

The type of access affects the parameters of the bean methods that are called by clients. The following sections apply not only to method parameters but also to method return values.



### 2.4.1.1  Isolation

The parameters of remote calls are more isolated than those of local calls. With remote calls, the client and the bean operate on different copies of a parameter object. If the client changes the value of the object, the value of the copy in the bean does not change. This layer of isolation can help protect the bean if the client accidentally modifies the data.

In a local call, both the client and the bean can modify the same parameter object. In general, you should not rely on this side effect of local calls. Perhaps someday you will want to distribute your components, replacing the local calls with remote ones.

As with remote clients, web service clients operate on different copies of parameters than does the bean that implements the web service.



### 2.4.1.2  Granularity of Accessed Data

Because remote calls are likely to be slower than local calls, the parameters in remote methods should be relatively coarse-grained. A coarse-grained object contains more data than a fine-grained one, so fewer access calls are required. For the same reason, the parameters of the methods called by web service clients should also be coarse-grained.

# 3.  Lificycle of Enterprise Bean

## 3.1  The Lifecycle of Stateful Session Bean

![jeett_dt_021](/jeett_dt_021.png)



While in the ready stage, the EJB container may decide to deactivate, or **passivate**, the bean by moving it from memory to secondary storage. (Typically, the EJB container uses a least-recently-used algorithm to select a bean for passivation.) The EJB container invokes the method annotated `@PrePassivate`, if any, immediately before passivating it. If a client invokes a business method on the bean while it is in the passive stage, the EJB container activates the bean, calls the method annotated `@PostActivate`, if any, and then moves it to the ready stage.

At the end of the lifecycle, the client invokes a method annotated `@Remove`, and the EJB container calls the method annotated `@PreDestroy`, if any. The bean's instance is then ready for garbage collection.

Your code controls the invocation of only one lifecycle method: the method annotated `@Remove`. All other methods in [Figure 32-2](https://docs.oracle.com/javaee/7/tutorial/ejb-intro007.htm#GIPMI) are invoked by the EJB container.



## 3.2  The Lifecycle of Stateless Session Bean

![jeett_dt_022](/jeett_dt_022.png)

The EJB container typically creates and maintains a pool of stateless session beans, beginning the stateless session bean's lifecycle. The container performs any dependency injection and then invokes the method annotated `@PostConstruct`, if it exists. The bean is now ready to have its business methods invoked by a client.

At the end of the lifecycle, the EJB container calls the method annotated `@PreDestroy`, if it exists. The bean's instance is then ready for garbage collection.

## 3.3  The Lifecycle of Singleton Session Bean

Like a stateless session bean, a singleton session bean is never passivated and has only two stages, nonexistent and ready for the invocation of business methods.

The EJB container initiates the singleton session bean lifecycle by creating the singleton instance. This occurs upon application deployment if the singleton is annotated with the `@Startup` annotation. The container performs any dependency injection and then invokes the method annotated `@PostConstruct`, if it exists. The singleton session bean is now ready to have its business methods invoked by the client.

At the end of the lifecycle, the EJB container calls the method annotated `@PreDestroy`, if it exists. The singleton session bean is now ready for garbage collection.

## 3.4  The Lifecycle of Message-Driven Bean

![jeett_dt_023](/jeett_dt_023.png)

The EJB container usually creates a pool of message-driven bean instances. For each instance, the EJB container performs these tasks.

1. If the message-driven bean uses dependency injection, the container injects these references before instantiating the instance.
2. The container calls the method annotated `@PostConstruct`, if any.

Like a stateless session bean, a message-driven bean is never passivated and has only two states: nonexistent and ready to receive messages.

At the end of the lifecycle, the container calls the method annotated `@PreDestroy`, if any. The bean's instance is then ready for garbage collection.







