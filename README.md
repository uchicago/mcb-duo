mcb-duo
=======

Shibboleth Multi-Context Broker Duo Authentication.  This is a plugin for the 
[Shibboleth Multi-Context Broker](https://wiki.shibboleth.net/confluence/display/SHIB2/Multi-Context+Broker). 
It provides support for [DUO](http://www.duosecurity.com/) second factor authentication.

# Installation
1. Copy the jar file to your shibboleth-source-dir/lib.  
2. run install.sh
3. Copy the *duo.vm* file to the directory holding the rest of your MCB velocity templates

# Configuration

Before you can configure, you will need to create a [WebSDK Integration](https://www.duosecurity.com/docs/duoweb) and 
generate an [Application Secret Key](https://www.duosecurity.com/docs/duoweb#1.-generate-an-akey).  After that you need
to edit the *mcb-spring.xml* file and add the following block.


    <bean id="mcb.duo" class="edu.uchicago.identity.mcb.authn.provider.duo.DuoLoginSubmodule">
        <!-- application key -->
        <constructor-arg index="0" value="APPKEY GOES HERE" />
        <!-- integration key -->
        <constructor-arg index="1" value="IKEY GOES HERE" />
        <!-- secret key -->
        <constructor-arg index="2" value="SKEY GOES HERE" />
        <!-- host -->
        <constructor-arg index="3" value="HOST GOES HERE" />
        <!-- duo login template -->
        <constructor-arg index="4" value="duo.vm" />
    </bean>
