Aplikacja służąca wysyłania powiadomień email utworzona dla Planszowe newsy. 
W celu poprawnej konfiguracji należy utworzyć zmienną systemową o nazwie "PN_EMAIL_PASSWORD", a nastepnie uruchomic 
ponownie komputer. 

Aby uruchomić aplikację, należy ją zbudować przy pomocy komendy "mvn clean package"

Następnie otwieramy konsolę w lokalizacji, gdzie jest plik z naszą zbudowaną aplikacją i uruchamiamy przy pomocy 
komendy: "java -jar your-application.jar", w tym wypadku: "java -jar pn-notification-1.0.jar"

W przypadku pierwszego uruchomienia aplikacji będzie ona wymagała pobrania danych autoryzacyjnych z google. W 
konsoli po uruchomieniu pokaże się odpowiedni link. Należy go kliknąć i zalogować się kontem do wysyłania emaili 
(noreply.planszowenewsy@gmail.com). Przy kolejnych uruchomieniach tegoż pliku JAR nie trzeba będzie tego już robić (lecz przy 
kolejnych nowych wersjach JAR'a-tak).

Aby skonfigurować zmienne należy przekopiować plik application.properties z GitHuba do lokalizacji pliku JAR i 
podmienić odpowiednie zmienne. 