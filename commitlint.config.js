module.exports = {
    parserPreset: {
        parserOpts: {
            headerPattern: /^(\w+), (\w+): (.+) #(\d+)$/,
            headerCorrespondence: ['developer', 'branch', 'subject', 'issue']
        }
    },
    rules: {
        'subject-empty': [2, 'never'], // 구현 내용이 비어 있으면 안 됨
        'header-max-length': [2, 'always', 72], // 제목 길이는 최대 72자
        'type-case': [2, 'always', 'lower-case'], // 브랜치명(타입)은 소문자여야 함
        'type-enum': [
            2,
            'always',
            ['feat', 'hotfix', 'chore'] // 허용되는 브랜치명 목록
        ]
    }
};
