MintYnabReconciler
==================

Diff tool for Mint and Ynab transaction files.  Mint tells you where your money went, and ynab tells you where your money is going.  Both are great tools, but sometimes I type in numbers incorrectly when I manually input transactions into ynab.  This tool is an excuse for me to play with technology, so excuse the overcomplicated architecture.

## Setup
This project uses grunt and bower for JavaScript development.
Run ```npm install``` to install grunt and it's dependencies, and run ```bower install``` to pull down the javascript libraries.

The 'watch' grunt target will watch the coffeescript directories and automatically compile coffeescript to the IntelliJ output directories.
