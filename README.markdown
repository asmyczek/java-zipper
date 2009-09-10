Zipper for Java
===============

## Description

It you are looking for a data structure to easy create, traverse and manipulate trees, try the `Zipper`. 
The `Zipper` is a cursor based data structure. A cursor into a tree is represented by a location object. A location references the node in focus and a context, which describes the position of the node inside the tree. Four basic methods `down()`, `up()`, `right()` and `left()` defined in the location object move the cursor to next location. The tree is manipulated at a location only. All possible update methods are defined in the location object as well.

A `Zipper` location can be seen as a local view into the tree. All (or almost all) tree changes are visible to this location only. The tree remains unchanged for other locations that reference the same tree. For more details see [`Zipper` data structure](http://en.wikipedia.org/wiki/Zipper_data_structure), included examples or unit tests.

## Build

Just build with `ant` and copy the resulting `zipper.jar` file into your class path.

## Getting started

Let us start with a simple `Node` and `Leaf` class:

<pre>
	// Node
    public class Node implements IZipNode {
    
      private String name;
      private final List&lt;Node&gt; children;
    
      public Node(final String name, final Node... children) {
        super();
    
        assert(name != null);
        assert(children != null);
    
        this.name = name;
        this.children = Arrays.asList(children);
      }
    
      public String getName() {
        return name;
      }
    
      public Collection&lt;Node&gt; getChildren() {
        return this.children;
      }
    
    }
    
	// Leaf
    public class Leaf extends Node {
    
      public Leaf(final String name) {
        super(name, new Node[0]);
      }
    
      @Override
      public Collection&lt;Node&gt; getChildren() {
        return null;
      }
    
    }
</pre>

Any zip-able class has to implement the `IZipNode` interface that only defines the `getChildren()` method. If `getChildren()` returns null, `Zipper` treats this node as a leaf node and throws a `ZipperException` on an attempt to add children. The base node always returns a collection, empty or not.

Using these classes we can create a sample tree:

<pre>
    /** 
     *       root 
     *        /\ 
     *       /  \ 
     *      a    d 
     *     / \    \ 
     *    /   \    \ 
     *   b     c    e 
     *
     * Or set notation:
     * root:[a:[b, c], d:[e]]
     */ 
    Node tree = new Node("root",
                  new Node("a", 
                    new Leaf("b"),
                    new Leaf("c")),
                  new Node("d",
                    new Leaf("e")));
</pre>

and zip it as following:

<pre>
    Loc&lt;Node&gt; root = Zipper.zip(tree);
</pre>

The `Zipper` constructor `zip()` creates a location `Loc` object that references the root node.

Let us do some moves and updates now:

<pre>
    // Move to node 'e';
    Loc&lt;Node&gt; e = root.down().right().down();
    
    // Add a sibling on the right
    Loc&lt;Node&gt; f = e.insertRight(new Leaf("f"));
    
    // Move up and add a sibling on the left
    Loc&lt;Node&gt; g = f.up().insertLeft(new Leaf("g"));
</pre>

Every move returns a new location object. Since tree changes remain local to a location, the resulting trees build from locations `e.root()`, `f.root()' and `g.root()` look like following (using `Main` class `printTree()` method from getting started examples):

<pre>
    Tree e: 
      root:[a:[b, c], d:[e]]
    Tree f:
      root:[a:[b, c], d:[e, f]]
    Tree g:
      root:[a:[b, c], g, d:[e, f]]
</pre>

The method `root()` calls `up()` recursively until the root node is reached and returns the root locations.
 
For more inside into the `Zipper` and this Java implementation see examples, unit tests and Javadoc.

Have fun!

## Zipper for non persistent data

Hopefully soon with a more in depth introduction to Zippers.

