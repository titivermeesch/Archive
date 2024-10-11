# Documentation Midaria

Cette documentation vous donnera toute les informations nécessaires pour contribuer au développement de Midaria.

## Structure des serveurs

Chaque mode de jeu a son serveur développement et un serveur de production. Les joueurs se trouvent sur le serveur production alors que les développeurs travaillent sur le serveur de développement.

Les différents serveurs sont disponibles via la commande `/server`.

## Structure des plugins

Une structure est mis en place pour regrouper les fonctionnalités communes et les foncionnalités spécifiques à un mode de jeu. Cette structure peut se résumer à:

- API: C'est une librairie qui contient toute les parties communes aux plugins (par exemple générer certaines particules, gèrer des messages,...)
- Core: C'est un plugin qui regroupe toute les fonctionnalités que chaque serveur doit avoir. Certains examples sont le système d'amis, le système de party, les reports,...

Ensuite suivent tout les plugins qui sont crée spécifiquement pour un mode de jeu.

Chaque plugin devra être écrit de la meilleure façon possible. Voici une liste des choses à respecter:

- Le package de chaque plugin devra suivre la structure de `fr.midaria.xxx`. Par exemple `fr.midaria.api`, `fr.midaria.core`, `fr.midaria.pvparena`,...
- Le plugin devra être écrit pour 1.8.8/1.8.9 avec Java 8
- Le plugin devra utiliser Gradle comme système de build
- Le plugin devra être écrit en Anglais uniquement (sauf les messages envoyés aux joueurs)

## Développer et tester un plugin

Les développeurs ont le droit de tester leurs plugins en local, et c'est aussi la meilleure façon pour faire du développement rapide.

Les développeurs doivent utiliser une branche GIT séparé pour faire leurs changements, et créer une MR (Merge Request, aussi connu sous le nom de Pull Request) vers la branche `main` pour proposer leurs changements. Une fois que ces changements se trouvent dans la branche `main`, les plugins seront mis à jour automatiquement sur le serveur dev (**fonctionnalité à venir**).

**Les développeurs n'auront en aucun cas accès au serveur FTP pour modifier les plugins directement**

## Mise en production d'un plugin

Le responsable développement se chargera de mettre en production tout les plugins de façon réguliêre.

## Librairies

Plusieurs librairies sont déjà mis en place pour aider les développeurs. Dans l'idéal, il faut utiliser les même librairies. Si jamais vous devez utiliser une autre librairie, veuillez le spécifier dans votre MR.

- Toute les fonctionalités dans l'API Midaria
- HolographicDisplay pour les hologrames
- ORMLite pour la gestion de la base de données
- SmartInvs pour les GUI (menus intéractifs)

----

_Document rédigé et maintenu par PlayBossWar_