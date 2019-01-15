<result>
    <name>${name}</name>
    <bean name="${bean.name}">
        <method>${bean.method()}</method>
        <method-with-param>${bean.methodWithParam("param")}</method-with-param>
    </bean>
    <substring>${name?substring(0, 2)}</substring>
</result>