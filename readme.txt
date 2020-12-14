Skladani CAM message:
=====================
Funkce Tools/ComposeMessage() sestavi zpravu jako pole bytu (5 + delka zpravy, max. 13 bytu)

POslani:
========
Funkce Tools/SendMessage() posle dodane pole bytu na zadany port a adresu

Knofliky:
=========
Funkce ve frmMain/SendXXXButton() zavola LaunchSequence() pro xxxPRESSED a xxxRELEASED.
LaunchSequence() jen sestavi (ComposeMessage()) a posle (SendMessage()) pro PRESSED
a po nejakem zpozdeni i RELEASED.

TOUCH zpravy:
=============
Finkce frmMain/SimulateTouch() sestavi a posle postupne vsechny typy zprav tak jak
se posilaly na BlackBox




ZMENY !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
===============================================
Zpravy, drive definovane pres databazi, jsou tady nadefinovane staticky primo v modulu
frmMain() a pro zpracovani v tomto programu maji minimalne button zpravy prehazene
poradi bytu - puvodni definice je zakomentarovana ve zdrojaku


