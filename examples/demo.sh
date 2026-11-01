#!/usr/bin/env bash
# Демонстрация обработки ошибок (этап 1, пункт 4)
cd "$(dirname "$0")/.."
./gradlew -q installDist
APP=build/install/Configuration_2/bin/Configuration_2
D=$(mktemp -d)
echo "A -> B" > "$D/repo.txt"

cfg() {
  printf '<config><package_name>%s</package_name><repository>%s</repository><mode>%s</mode></config>' \
    "$2" "$3" "$4" > "$D/$1.xml"
}
cfg bad_mode serde https://crates.io fast
cfg bad_name "bad name!" https://crates.io remote
cfg bad_url serde ftp://crates.io remote
cfg lower_in_test serde "$D/repo.txt" test
cfg no_repo A no/such/repo.txt test
printf '<config><package_name>serde</package_name></config>' > "$D/no_mode.xml"
printf '<config><package_name>serde</package_name>' > "$D/broken.xml"

run() { echo; echo "=== $1"; shift; "$APP" "$@"; echo "код возврата: $?"; }
run "mode: недопустимое значение" "$D/bad_mode.xml"
run "mode: параметр не задан" "$D/no_mode.xml"
run "package_name: недопустимые символы" "$D/bad_name.xml"
run "package_name: строчные буквы в режиме test" "$D/lower_in_test.xml"
run "repository: плохая схема URL" "$D/bad_url.xml"
run "repository: файл не найден (test)" "$D/no_repo.xml"
run "файл конфигурации: битый XML" "$D/broken.xml"
run "файл конфигурации: не найден" no/such/file.xml
run "аргументы: слишком много" a.xml b.xml