# Multi-Vendor SMS Sending Platform

[中文说明](./README_CN.md)

---

## Introduction
This repository provides a **multi-vendor SMS sending platform**, designed to support seamless integration with different SMS service providers.  
It currently includes core implementations for **retry mechanisms**, **load balancing**, and **disaster recovery**, though the codebase is **not yet runnable** since configuration and setup files are still incomplete.

Initial designs for **persistence** and **message history tracking** have also been included, but are not yet functional.

---

## Features
1. **Abstraction Layer**  
   Provides a unified interface for different SMS vendor APIs, ensuring consistency and ease of replacement or extension.

2. **Configuration Management**  
   Each vendor may have unique API endpoints, authentication methods, and template formats.  
   A centralized configuration system or file should manage parameters such as URLs, credentials, and template definitions.

3. **Disaster Recovery**  
   Since SMS delivery is time-sensitive, if one vendor’s service fails, the system should quickly switch to another available provider.

4. **Load Balancing**  
   For large-scale applications, distribute requests across multiple vendors to maximize throughput and stability.

5. **(TODO) Monitoring & Alerts**  
   Implement real-time monitoring to collect metrics such as success rates and delivery latency.  
   If a vendor’s performance drops below a threshold, trigger alerts and automatically reroute messages.  
   Logs or database tracking should support this feature.

6. **Retry Mechanism**  
   When message sending fails, retry up to N times with exponential backoff or adjustable intervals.

7. **Data Persistence**  
   Store all message details — including content, status, and metadata — in a database for later analysis and archiving.

8. **Delivery Status Tracking**  
   Enable querying of message delivery states, crucial for cases that require confirmation of receipt.

9. **Circuit Breaker**  
   If a vendor repeatedly fails beyond a threshold, temporarily remove it from the rotation to protect overall stability.

---

## Roadmap
- [ ] Complete configuration system
- [ ] Add database persistence layer
- [ ] Implement monitoring dashboard
- [ ] Provide unified API documentation

---

## License
MIT License
