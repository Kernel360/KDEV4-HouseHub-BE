module.exports = {
    parserPreset: {
        parserOpts: {
            headerPattern: /^(.+)?,\s+(.+)?:\s+(.+)?\s+#(.+)?$/,
            headerCorrespondence: ['developer', 'type', 'subject', 'issue'],
            issuePrefixes: ['#']
        }
    },
    plugins: [
        {
            rules: {
                'developer-format': ({ developer }) => {
                    const dev = developer ?? '';
                    const isValid = dev.trim().length >= 3 && /^[a-zA-Z]+$/.test(dev);
                    return [isValid, '개발자 이름은 3자 이상 영문이어야 합니다'];
                },
                'type-enum': ({ type }) => {
                    const realType = type ?? '';
                    const allowedTypes = ['feat', 'hotfix', 'chore','feature','fix', 'docs'];
                    return [allowedTypes.includes(realType), `타입은 ${allowedTypes.join(', ')} 중 하나여야 합니다`];
                },
                'subject-empty': ({ subject }) => {
                    const realSubject = subject ?? '';
                    return [
                        realSubject.trim().length > 0,
                        '구현 내용은 필수 입력 항목입니다 (예: "프로젝트 초기 설정")'
                    ];
                },
                'issue-format': ({ issue }) => {
                    const realIssue = issue ?? '';
                    const isValid = /^\d+$/.test(realIssue);
                    return [isValid, '이슈 번호는 #1234 형식이어야 합니다'];
                },
            }
        }
    ],
    rules: {
        'header-max-length': [2, 'always', 100], // 메세지의 최대 길이 100글자
        'developer-format': [2, 'always'], // 개발자 이름은 3글자 이상 영문
        'type-enum': [2, 'always'], // feat,hotfix,chore 중 택 1
        'subject-empty': [2, 'never'], // subject가 비어있으면 안됨
        'issue-format': [2, 'always'] // issue는 # + 아라비아 숫자여야함
    }
};