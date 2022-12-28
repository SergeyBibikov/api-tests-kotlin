test:
	-./gradlew clean test
	-./gradlew allureServe

serve:
	-./gradlew allureServe

clean:
	-./gradlew clean