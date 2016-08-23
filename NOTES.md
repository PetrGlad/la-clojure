##### Surrond with {},[],()
 
Can be implemented as live template.
There are already universally applicable templates in 
basic Idea for () and {}. Make clojure-specific ones? 
Is it possible to parse in template? Or make a macro-like action?

##### REPL
Fix for history ordering is due in closed class 
- I do not want to mess with pull requests (they have 4 year old PRs not closed). 
Options (not mutually exclusive): 
1. Can copy-paste what's necessary for console.
2. Implement simpler console without user input and use a scratch file for eval.
3. Append ouptut direclty to scratch file (how to handle large outputs then?). 
Inline eval feature like in Java files can be useful also.

##### Misc 
* Fix new clojure file template. It is there in the code but not in menus or actions
* Automate "lein pom" hack (it seems reasonable and usable).
* Do we need builder service or just launch lein? HOw to handle output then 
(parse text looks unreliable)?