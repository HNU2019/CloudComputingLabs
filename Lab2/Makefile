all: javasever

javasever:
	javac src/server/Main.java -d out
clean:
	find out/src -name "*.class" | xargs rm -rf
	rm -rf out/src
