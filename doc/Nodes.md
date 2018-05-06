# Logical nodes

Almost everything in PSB application (screens, entities, components, systems, configurations) is represented as a tree of `LogicalNode`s in runtime.

You may think of `LogicalNode`s tree as of a virtual filesystem in UNIX-like OS:

* There is a root node (called also "application root").
    Application root is accessible as dependency through IoC container (dependency name is `"application root node"`, it requires no parameters and usually is a singleton).

* Node may be a "directory" - it may have child nodes associated with identifiers unique within a set of children of the same node.
    Such nodes are called "groups".

* Node may be a "file" (or "leaf") in this case it means that it has an object (called "component") attached

* Node may be a "directory" and a "file" at the same time.
    Okay.
    This is a bit different from filesystems.

* It's possible to reach one node from another using a "path" - a sequence of identifiers.
    Reference to parent node (i.e. pathstep "..") is a special case - there is no identifier meaning "parent of this node" so another method should be used to access parent node.

* It's possible to implement symbolic (not implemented, but possible) and hard (see `ProxyNode`) links.

*__Note about naming:__ they are called `LogicNodes` because they represent parts of logical structure of application.
Name `Node` could cause conflicts in the future as trees and graphs are very widely used in different kinds of systems (there may be `SceneNode`s - nodes of visual scene tree, `NavGraphNode`s - nodes of navigation graph in some kind of game-AI system, etc).*

## Declarative representation

`LogicNode`s represent logical structure of application at runtime but there also is ability to serialize and deserialize state of the application.



Ideal PSB application is created with 0 lines of code (it also has a shape of sphere and flies in vacuum at speed of light)!

### Initialization order problem

The situation described below caused some troubles with previous version of PSB and was one of reasons to start new version.

Assume we have a declarative representation of list of children of some node:

```JavaScript
{
    "child1": {
        "class": "class1",
        // ... //
    },
    "child2": {
        "class": "class2",
        // ... //
    },
    "child3": {
        "class": "class3",
        // ... //
    }
}
```

Now assume that we initialize children in the order they are declared and `"child2"` needs `"child3"` to be initialized.
So we have to change the order they are declared within:

```JavaScript
{
    "child1": {
        "class": "class1",
        // ... //
    },
    "child3": {
        "class": "class3",
        // ... //
    },
    "child2": {
        "class": "class2",
        // ... //
    }
}
```

But as the order starts to matter the description stops being pure declarative.
It becomes a bit imperative instead.

`LogicNode`s solve this problem by providing a `populate()` operation inserting a set of children with unknown cross-dependencies.

## Tree context

`TreeContext` is just an object holding reference to tree root and some additional object required do interact with tree.
Tree context is available from any node of tree.

## Operations

Here is a list of all operations provided by public API of `LogicNode`s:

|     Operation     |        Method      |           Description          |
|-------------------|--------------------|--------------------------------|
| Get parent        | `getParent()`      | Returns parent of the node |
| Get tree context  | `getTreeContext()` | Returns context of the tree the node belongs to |
| Get attached component **(1)** | `getComponent()` | Returns a component attached to the node |
| Get child         | `get(id)`          | Returns a child by it's identifier |
| Get or create child **(1)** | `getOrAdd(id,factory,arg)` | Returns a child previously added with given identifier or tries to create a new one using given factory |
| Remove a child by id | `remove(id)` | Removes child with given identifier or **does nothing** when there is no such child |
| Remove a child | `remove(child)` | Removes the given node from list of children ot **throws exception** when there is no such child |
| Add group of children nodes **(1)** | `populate(populator)` | Adds a group of child nodes (the nodes may have dependencies on other nodes from the same group) |
| Accept a visitor | `accept(visitor)` | Accept a visitor (of type `NodeVisitor`) |

**(1)** - these operations may throw `UnsupportedOperationException` when node is of the type these operations have no sense for.
