# Spring Demo Project

This demo uses Java 8, Gradle 5, and the latest stable release of Spring Boot, Spring MVC, Spring Cache, Spring Messaging and Spring data. It also uses the lastest React.js coded in ES6 with webpack. 

 
[![Build Status](https://travis-ci.org/chifei/spring-demo-project.svg?branch=master)](https://travis-ci.com/chifei/spring-demo-project)
[![Code Coverage](https://codecov.io/gh/chifei/spring-demo-project/branch/master/graph/badge.svg)](https://codecov.io/gh/chifei/spring-demo-project)
[![Code Quality: Java](https://img.shields.io/lgtm/grade/java/g/chifei/spring-demo-project.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/chifei/spring-demo-project/context:java)
[![Total Alerts](https://img.shields.io/lgtm/alerts/g/chifei/spring-demo-project.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/chifei/spring-demo-project/alerts)


## Try the demo

[https://demo2.jweb.app/](https://demo2.jweb.app/)<br>
admin/admin

> Database will be reset every 10 minutes.


## Features

- User login/logout
- User switch language (i18n for English and Chinese)
- User search user by role or name
- User create or update another user, assign roles.
- User delete another user with batch delete support
- User search role by name
- User create or update another role, assign permissions.
- User delete another role with batch delete support.
- User can only see buttons/menus with permission granted.
- Upload products with CSV files.
- Export products to CSV files.

<img src="doc/login.png"/>
<img src="doc/list.png"/>


## ACL implementation

- @LoginRequired and @PermissionRequired
- Corresponding LoginRequiredInterceptor and PermissionRequiredInterceptor
- React PermissionRequired Component

```java

@RequestMapping(value = "/admin/api/user/find", method = RequestMethod.PUT)
    @PermissionRequired("user.read")
    public UserQueryResponse find(@RequestBody UserQuery userQuery) {
        UserQueryResponse userQueryResponse = new UserQueryResponse();
        userQueryResponse.items = items(userService.find(userQuery));
        userQueryResponse.page = userQuery.page;
        userQueryResponse.limit = userQuery.limit;
        userQueryResponse.total = userService.count(userQuery);
        return userQueryResponse;
    }

    @RequestMapping(value = "/admin/api/user", method = RequestMethod.POST)
    @PermissionRequired("user.write")
    public UserResponse create(@RequestBody CreateUserRequest request) {
        request.requestBy = userInfo.username();
        return response(userService.create(request));
    }

```

```javascript
<PermissionRequired permissions={["user.write"]}>
    <Button type="text"> <Link to={{pathname: "/admin/user/role/" + data.id + "/update"}}>{i18n.t("user.update")}</Link> </Button>
</PermissionRequired>
<PermissionRequired permissions={["user.write"]}>
    <Button onClick={e => this.delete(data, e)} type="text">{i18n.t("user.delete")}</Button>
</PermissionRequired>
```