
# libgen_Automation


------------------------------------------------------
TL;DR - Summary: 
A simple demonstration of browser automation and general programming concepts on three different websites using Selenium in Java. Libgen.fun, Open Library and Project Gutenberg are all included.
------------------------------------------------------

GNU GENERAL PUBLIC LICENSE


Please note before using this project that not all of the books and articles on these sites (particularly LibGen.fun) will be public domain. I am by no means advocating the piracy of copyright materials. This project is a demonstration only. 

Some programming features include:
- Use of Selenium for browser automation - CSS & HTML targeting via Xpath and CSSSelect
- OOP concepts
- Regular Expressions

The possibilities are boundless. Tech support workers can use this type of technology to streamline their maintenance tasks. Large tech companies are always in need of quality assurance and penetration testing on their own apps. The more diversified the individual, the more they will realize can be automated. Even in jobs nobody would ever really expect, like payroll, data entry, customer service and ticketing systems... One of the things I have been looking into doing is the full automation of a drop shipping company right down to the filing of taxes. However, I am the type that just has way too much fun with these things!

If you run this project or decide to add to it, you will probably want to change the key terms it searches with. I would suggest routing this as a parameter through the main method.

What It Does…

This is a fairly simple demonstration of browser/web app automation. It opens up Chrome and navigates to each site individually, injecting the query into the URL.

The first thing to take place is a collection of page results as URLs, this is used to get ALL of the books in a given search result.
Once the query loads, it iterates through a table or Div elements and performs a click event on each of the book links. This loads another page with the specific details about the author, ISBN, page count, publisher and more. Those details are collected into a text file delimited by “|”. This may seem odd but a lot of the titles and author fields have commas in them. Regular expressions are used to extract and clean the data.

Libgen does something rather different with downloads. They seem to go into a buffer rather than download directly to your hard drive with one click.

The roundabout methodology of libgen.fun makes it a fairly ideal candidate to show off browser automation for web apps.

After each download, the next link in the list is processed. Once all of the links on a given page are done, it will continue like this through each of the page results.

An automation process for Project Gutenberg has also been added with a few minor changes to the approach.

My internet access is awful at the moment as I am preparing to relocate. As such, I haven’t fully tested the entire process. I just made sure that the first few links were working as intended for each automation.

There are some other things I would like to add when I have more time.

I would like to highlight each step of the process as it unfolds. This is a demo after all!
I would also like to modify the project to support and distinguish between research articles and educational books.

Later, I may make a class that processes all of the textfiles into a Maria database (open source MySQL).

Why the interest in books and articles?

I have actually done a whole lot of natural language processing with prior projects. I tend to do a lot of web scraping/crawling and browser automation to target highly specific details for graph data and semantic analyses.

As such, I may be uploading things like this on a regular basis if people show interest.

Recently, I started looking at the integrity of data contained in books and articles vs the somewhat random quality of what may be found online. People tend to put a lot less effort into blog posts and news is largely just sensational with little actual sourcing of the details. In my more eleborate NLP projects, I do semantic correlations of categorical information across large bodies of data.

