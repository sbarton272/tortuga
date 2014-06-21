JC = javac
JFLAGS = -g -cp ".:weka.jar"
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java
CLASSES = StartWeka.java
default: classes
classes: $(CLASSES:.java=.class)
clean:
	/bin/rm *.class
wine:
	java -cp ".:weka.jar" StartWeka "winequalitywhite-data/train.arff"
amazon:
	java -cp ".:weka.jar" StartWeka "amazon-data/train.arff"
