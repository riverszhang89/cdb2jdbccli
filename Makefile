JAVAC=
JAVA=
ifeq ($(JAVA_HOME),)
JAVAC=$(shell which javac 2>/dev/null)
JAVA=$(shell which java 2>/dev/null)
else
JAVAC='$(JAVA_HOME)/bin/javac'
JAVA='$(JAVA_HOME)/bin/java'
endif

Client.class: Client.java
	@$(JAVAC) -cp ~/.m2/repository/com/bloomberg/comdb2/cdb2jdbc/2.0.0/cdb2jdbc-2.0.0-shaded.jar Client.java

call:
	@$(JAVA) -cp ~/.m2/repository/com/bloomberg/comdb2/cdb2jdbc/2.0.0/cdb2jdbc-2.0.0-shaded.jar:$(PWD) Client "//$(CLUSTER)/$(DB)" -

alias:
	@echo alias cdb2jdb="'$(JAVA) -cp ~/.m2/repository/com/bloomberg/comdb2/cdb2jdbc/2.0.0/cdb2jdbc-2.0.0-shaded.jar:$(PWD) Client'"
	@echo "Add the line above ^^^^^^ to profile."

clean:
	@rm Client.class
