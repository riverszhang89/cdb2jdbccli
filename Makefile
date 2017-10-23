Client.class: Client.java
	$(JAVA_HOME)/bin/javac -cp ~/.m2/repository/com/bloomberg/comdb2/cdb2jdbc/2.0.0/cdb2jdbc-2.0.0-shaded.jar Client.java

call:
	@$(JAVA_HOME)/bin/java -cp ~/.m2/repository/com/bloomberg/comdb2/cdb2jdbc/2.0.0/cdb2jdbc-2.0.0-shaded.jar:$(PWD) Client "//$(CLUSTER)/$(DB)" -

alias:
	@echo alias cdb2jdb="'$(JAVA_HOME)/bin/java -cp ~/.m2/repository/com/bloomberg/comdb2/cdb2jdbc/2.0.0/cdb2jdbc-2.0.0-shaded.jar:$(PWD) Client'"
	@echo "Add the line above ^^^^^^ to profile."
