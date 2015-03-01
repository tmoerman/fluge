# Flüge

![image][logo]

[logo]: img/fluge.png

A Quil sketch implementing an interactive "brushing and linking" visualization, inspired by the flight data [visualization exercise](http://homes.esat.kuleuven.be/~bioiuser/blog/hands-on-data-visualization-using-processing/) on the [VDA-lab blog](http://vda-lab.be).

## Usage

You'll need the [Leiningen](http://leiningen.org/) build tool to compile the code.

Run `lein compile` command and open `index.html` in your browser.

For interactive development run `lein cljsbuild auto` command. This command will be recompiling cljs to js each time you modify `core.cljs` and you can see result immediately by refreshing page.

