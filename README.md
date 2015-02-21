MintYnabReconciler
==================

Diff tool for Mint and Ynab transaction files.  Mint tells you where your money went, and ynab tells you where your money is going.  Both are great tools, but sometimes I type in numbers incorrectly when I manually input transactions into ynab.  This tool is an excuse for me to play with technology, so excuse the overcomplicated architecture.

## Setup
This project uses grunt for JavaScript development.  You can find more info about grunt here: http://gruntjs.com/getting-started
For each of the dependencies in package.json, run: ```npm install <name> --save-dev``` to install the dependency.

## Developing
I write code in IntelliJ - open Gruntfile.coffee in the grunt console and execute the watch task to auto-compile coffeescript.
