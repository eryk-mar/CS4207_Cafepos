Smells Removed:

    God Class split into focused services

    Primitive Obsession replaced with strategy patterns

    Global State eliminated via dependency injection

    Shotgun Surgery prevented via centralized pricing logic

Refactorings Applied:

    Extract Class for policies and services

    Replace Conditional with Polymorphism

SOLID Principles:

    Single Responsibility through focused classes

    Open/Closed via extensible interfaces

Extensibility:
New discounts implement DiscountPolicy interface
No existing code modifications required

Why is adapting the legacy printer better than changing your domain or vendor class?

Because we have two systems, one is our domain system (use String-based printing), another one is vendor system (use byte[]).
Changing domain class would pollute clean, modern code with old technical details. Changing vendor class would break other clients who's using it, and we don't own vendor's code.
So we introduce adapter to transfer between the two.


Trade-offs: Layering vs Partitioning

• Why did you choose a Layered Monolith (for now) rather than partitioning into multiple services?
The current scope of the Café POS & Delivery system is small and tightly integrated. By not starting with microservices, we avoid the complexity of distributed systems.
Since the module is about learning design patterns, a layered monolith allows us to concentrate on clean architecture, cohesion, and maintainability.
It also simplifies testing and development, enabling faster iteration while the domain model is still evolving.

ADR: Use Layered Monolith Architecture

Context:
The current scope of the Café POS & Delivery system is small and tightly integrated.
By not starting with microservices, we avoid the complexity of distributed systems.
Since the module is about learning design patterns, a layered monolith allows us to concentrate on clean architecture,
cohesion, and maintainability.
It also simplifies testing and development, enabling faster iteration while the domain model is still evolving.

Decision:
Use a Layered Monolith with Presentation, Service, Domain, and Infrastructure layers.

Alternatives:
- Microservices
- Modular Monolith

Rationale:
A monolith keeps the application simple, reduces deployment overhead, avoids distributed-system complexity,
and allows us to focus on clean design patterns.

Consequences:

Pros
- Easy to develop and test
- No network overhead or service deployment
- Clean boundaries inside the codebase
- Faster iteration while the domain is still evolving
- Layered structure keeps the option open for future extraction into services

Cons
- Not independently deployable
- Harder to scale individual components
- If the system grows very large, extraction work will be needed