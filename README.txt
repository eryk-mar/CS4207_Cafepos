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

Which seams are natural candidates for future partitioning (e.g., Payments, Notifications)?
Payments, alerts, and reporting are obvious choices for service extraction in the future.
Payments have specific security requirements (in the real world) and interact with external gateways. Multiple channels with different scaling requirements are managed by notifications.
 Reporting is a data-intensive process that requires specialized resources.
 Since order management and product catalog represent different business domains, they may also develop into stand-alone services.

What are the connectors/protocols you would define if splitting (events, REST APIs)?
We would use REST APIs with versioned contracts for synchronous operations like payments.
For dependable delivery, asynchronous events like order status changes would pass through message brokers like Kafka.
For performance-critical interactions, gRPC may be used in internal service communication, with API gateways handling external access points.

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