<div align="center">

<!-- logo -->

![image](https://github.com/user-attachments/assets/c6d2b941-83d1-4ad3-8c17-451705a55fe1)

### HouseHub - 고객 관리의 모든 것을 하나로 

[<img src="https://img.shields.io/badge/-readme.md-important?style=flat&logo=google-chrome&logoColor=white" />]()  [<img src="https://img.shields.io/badge/release-v1.0.0-yellow?style=flat&logo=google-chrome&logoColor=white" />]()
<br/> [<img src="https://img.shields.io/badge/프로젝트 기간-2025.03.17~2025.05.16-green?style=flat&logo=&logoColor=white" />]()

</div>

## 시연 영상
[![프로젝트 시연 영상](https://img.youtube.com/vi/NZXvngEb1k0/0.jpg)](https://github.com/user-attachments/assets/4f39d132-417e-47da-b14a-aeb3be425b21)
 
## 배포 링크
https://www.house-hub.store

## 📝 프로젝트 소개
**HouseHub**은 공인중개사들의 실무를 디지털화하고, 고객과의 응대 흐름을 체계적으로 정리하여 **중개인의 업무 효율과 계약 성사율을 동시에 향상시키는 부동산 CRM 플랫폼**입니다.
특히 수기로 관리하던 고객과 매물 정보를 한 플랫폼에서 통합 관리할 수 있도록 하여, **작은 규모의 중개 사무소도 쉽게 도입할 수 있는 현장 중심형 고객 관리 시스템**으로 설계되었습니다.

## 🚀 주요 기능
- 고객 문의부터 상담, 매물 추천, 계약 체결까지 전 과정 통합 관리
- 고객 맞춤형 상담 히스토리 및 추천 매물 연결
- 대시보드 제공을 통한 실시간 업무 현황 시각화
- 문자 발송 시스템과 템플릿으로 고객 커뮤니케이션 자동화
- 고객/매물/상담/계약을 유기적으로 연결한 통합 관리 기능
- 삭제 복구 기능 및 엑셀 업로드 지원으로 기존 자료 쉽게 이전 가능
- 외부 플랫폼의 매물 정보 제공 기능 (직방, 네이버 등)

### 📌 Infra
<img width="565" alt="Screenshot 2025-05-21 at 10 05 05 AM" src="https://github.com/user-attachments/assets/778beb92-5aac-47bc-85fd-da7ca49e7f65" />

1. **GitHub Actions**
    - GitHub Actions를 활용해 CI/CD 파이프라인 구성
    - develop 브랜치 푸시 시: CI 단계 (빌드 및 테스트) 실행
    - main 브랜치 병합 시: 자동 배포 진행
    - 무조건 자동 배포되지 않도록 설계하여 개발자가 배포 시점을 제어할 수 있도록 구현
2. **Docker**
    - 백엔드 서버를 Docker로 컨테이너화하여 환경 일관성 및 이식성 확보
3. **AWS EC2**
     - 백엔드 서버를 AWS EC2에 배포하여 자체 운영 환경 구성

4. **Vercel**
     - 프론트엔드 애플리케이션을 Vercel에 배포하여 간편한 정적 배포 및 프론트 CI/CD 구성
---

### 📌 기타 기능
1. **사용자 인증 및 권한 관리**
    - Redis 기반 세션을 활용하여 로그인 및 권한 관리 구현
    - 인증 정보는 서버 측 세션에 저장되며, 세션 키는 클라이언트에 전달
2. **실시간 알림**
    - 고객이 문의 폼을 작성하면 해당 중개인에게 SSE(Server-Sent Events) 방식으로 실시간 알림 전송
3. **메일 전송**
    - SMTP를 활용하여 인증 메일 등 다양한 알림 메일 전송 기능 구현

## 🗂️ 기술 문서
👉 **API 명세서** : [바로가기](https://api.house-hub.store:8443/swagger-ui/index.html)

👉 **기능 명세서** : [바로가기](-)

👉 **ERD** :

## 💁‍♂️ 프로젝트 팀원

| 이름  | 역할           | 담당 업무                                                                                                   |
|-----|--------------|---------------------------------------------------------------------------------------------------------|
| 박병찬 | 백엔드 개발자 (팀장) | 백엔드 CI/CD 구축<br/> Agent, Consultation, Inquiry, InquiryTemplate, Notification, Dashboard API 개발 <br/> SpringSecurity, Spring SMTP |
| 김현호 | 백엔드 개발자      | 네이버 부동산, 직방 등 외부 플랫폼에서 약 30만 건의 매물 정보 수집, CrawlingProperties API 개발<br/>                                            |
| 이영석 | 백엔드 개발자      | Customer, Customer History, Sms API 개발 <br/>                                                         |
| 허성은 | 백엔드 개발자      | Property, Contract API 개발 <br/>                                                            |

