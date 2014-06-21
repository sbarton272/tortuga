JC = javac
JFLAGS = -g -cp ".:weka.jar:libsvm3.jar"
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java
CLASSES = StartWeka.java
default: classes
classes: $(CLASSES:.java=.class)
clean:
	/bin/rm *.class
run:
	java -cp ".:weka.jar:libsvm3.jar" StartWeka
#car:
#	java -cp ".:weka.jar:libsvm3.jar" StartWeka "car-data/train.arff"
#wine:
#	java -cp ".:weka.jar:libsvm3.jar" StartWeka "winequalitywhite-data/train.arff"
