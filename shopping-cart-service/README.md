# Shopping Cart Service

The shopping cart service is responsible for providing an API that manages the products that a user has chosen to add to their online shopping cart.

## Event Sourcing

Event sourcing is a software pattern where the state of data in a domain is captured and stored as a sequence of append-only events. Instead of storing the state of a data entity in a column or a property value, the state will be aggregated from a sequence of events. This service uses event sourcing to aggregate the state of a user's shopping cart.