cd baritone-1.20.2
git pull
./gradlew build
# now move it to the libs dir for the next build of the mod
mkdir ../libs
cp build/libs/baritone-common-1.10.2.jar ../libs/baritone.jar
cd ../..
./gradlew build --refresh-dependencies