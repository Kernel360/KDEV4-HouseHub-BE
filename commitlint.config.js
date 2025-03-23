module.exports = {
    parserPreset: {
        parserOpts: {
            headerPattern: /^(.+),\s+(.+):\s+(.+)\s+#(.+)$/,
            headerCorrespondence: ['developer', 'type', 'subject', 'issue'],
            issuePrefixes: ['#']
        }
    },
    plugins: [
        {
            rules: {
                'issue-format': ({ issue }) => {
                    const issueNumber = issue.match(/(\d+)$/);
                    return [
                        issueNumber && parseInt(issueNumber[1]) > 0,
                        `이슈 번호는 #(아라비아 숫자) 형식이어야 합니다 (예: #1)`
                    ];
                },
                'developer-format': ({ developer }) => {
                    const name = developer.match(/^(\w+)$/);
                    return [
                        name && name[1].length >= 3,
                        `개발자 이름은 3자 이상 영문이어야 합니다 (예: byungchan)`
                    ];
                },
                'type-enum': ({ type }) => {
                    const allowedTypes = ['feat', 'hotfix', 'chore'];
                    return [
                        allowedTypes.includes(type),
                        `타입은 ${allowedTypes.join(', ')} 중 하나여야 합니다.`
                    ];
                }
            }
        }
    ],
    rules: {
        'header-max-length': [2, 'always', 72],
        'developer-format': [2, 'always'],
        'type-enum': [2, 'always'],
        'subject-empty': [2, 'never'],
        'issue-format': [2, 'always']
    }
};
